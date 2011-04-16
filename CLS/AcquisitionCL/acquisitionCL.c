/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Command line interface for Data Acqusition library (0.2.7 >= libacq.so < 0.3.0)
 *
 */

#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <epicsConnect.h>
#include <acquisitionLib.h>

#define ACL_PROGNAM "acquisitionCL"
#define ACL_VERSION "0.1.2 (libacq.so.2.7.0, libEpicsConnect.so.1.5.2)"

#define CMD_BUFFER_SIZE 128
#define MACRO_BUFFER_SIZE 512

void printHelp();
void printVersion();

void showMessage(acqMaster_t *master, char *fmt, ...);
void showStatus(acqMaster_t *master, acqState mode);
void showProgress(acqMaster_t * master, char *title, double percent);
void onStop(acqMaster_t *master);

void acqStart();
void acqStop();
void acqPause();
void acqResume();
void acqQuit();

int isAcqStarted();
int isAcqPaused();

void processCommands();

void loadMacro(const char *macro);
void loadMacroFile(FILE *macroFile);
void printMacros();

acqMaster_t *master;

int main(int argc, char *argv[]) {

	const char *optstring = "m:M:f:d:n:s:e:p:i";
	char optprint[] = {0, 0};
	int optargc;
	char **optargv;

	int opt = 0;
	int interactive = 0;

	FILE *cfgFile;
	char *cfgFileName;

	FILE* macroFile;

	acqScan_t *scan = NULL;

	if(argc <= 1)  {
		printHelp();
		return 1;
	}

	if(!(strcasecmp(argv[1],"-h") && strcasecmp(argv[1], "--help"))) {
		printHelp();
		return 1;
	}

	if(!strcasecmp(argv[1],"--version")) {
		printVersion();
		return 1;
	}
	
	cfgFileName = argv[1];
	cfgFile = fopen(cfgFileName, "r");
	if(cfgFile == NULL) {
		fprintf(stderr, "Error: Could not open configuration file: %s\n", argv[1]);
		fflush(stderr);
		return 1;
	}
	fclose(cfgFile);

	master = new_acqMaster();
	master->acqStatusMessage = showMessage;
	master->putMode = showStatus;
	master->globalShutdown = 1;
	master->globalPause = 0;

	file_load(cfgFileName, master);
	
	opterr = 0;
	optargc = argc - 1;
	optargv = argv + 1;

	while((opt = getopt(optargc, optargv, optstring)) >= 0) {

		switch (opt) {
			case 'f':
				master->acqFile->fileTemplate = optarg;
				break;

			case 'd':
				master->acqFile->dirname = optarg;
				break;

			case 'm':
				loadMacro(optarg);
				break;
				
			case 'M':
				macroFile = fopen(optarg, "r");
				if(!macroFile) {
					fprintf(stderr, "Error: Macro file not found: %s\n", optarg);
					fflush(stderr);
					return -1;
				}
				loadMacroFile(macroFile);
				fclose(macroFile);
				break;

			case 'i':
				interactive = 1;
				break;

			case 'n':
				scan = lookup_acqScan(optarg, master);
				if(scan == NULL) {
					fprintf(stderr, "Error: Scan not found: %s\n", optarg);
					fflush(stderr);
					return -1;
				}
				break;

			case 's':
				if(scan == NULL) {
					fprintf(stderr, "Error: No scan selected. Cannot set start value.\n");
					fflush(stderr);
					return -1;
				}
				if(scan->acqControlList == NULL) {
					printf("Error: Scan %s has no controls defined!\n", scan->scanName);
					fflush(stderr);
					return -1;
				}
				scan->acqControlList->startVal = atof(optarg);
				scan->acqControlList->haveStartVal = 1;
				break;

			case 'e':
				if(scan == NULL) {
					fprintf(stderr, "Error: No scan selected. Cannot set end value.\n");
					fflush(stderr);
					return -1;
				}
				if(scan->acqControlList == NULL) {
					printf("Error: Scan %s has no controls defined!\n", scan->scanName);
					fflush(stderr);
					return -1;
				}
				scan->acqControlList->finalVal = atof(optarg);
				scan->acqControlList->haveFinalVal = 1;
				break;

			case 'p':
				if(scan == NULL) {
					fprintf(stderr, "Error: No scan selected. Cannot set end value.\n");
					fflush(stderr);
					return -1;
				}
				if(scan->acqControlList == NULL) {
					printf("Error: Scan %s has no controls defined!\n", scan->scanName);
					fflush(stderr);
					return -1;
				}
				scan->acqControlList->deltaVal = atof(optarg);
				scan->acqControlList->haveDeltaVal = 1;
				break;

			case '?':
					optprint[0] = (char) optopt;
					fprintf(stderr, "Error: Unexpected option: -%s\n", optprint);
					fflush(stderr);
					printHelp();
					return -1;

			default:
					fprintf(stderr, "Error: Unhandled option: %#x\n", opt);
					fflush(stderr);
					printHelp();
					return -1;
		}	
	}

	//acqSetShowProgress(showProgress, master);
	//acqSetOnStop(onStop, master);

	if(interactive) {
		processCommands();
	}
	else {
		acqStart();
	}
	
	while(isAcqStarted()) {
		usleep(500);
	}

	return 0;
}

void processCommands() {

	const char *startCmd = "start\n";
	const char *stopCmd = "stop\n";
	const char *pauseCmd = "pause\n";
	const char *resumeCmd = "resume\n";
	const char *quitCmd = "quit\n";

	char cmdBuffer[CMD_BUFFER_SIZE];
	char *cmd = strcpy(cmdBuffer, "");
	
	int process = 1;

	while(process) {

		fgets(cmdBuffer, CMD_BUFFER_SIZE, stdin);

		if(!strcasecmp(cmd, quitCmd)) {
			if(isAcqStarted()) {
				acqStop();
			}
			acqQuit();
			process = 0;
		}
		else if(!strcasecmp(cmd, startCmd)) {
			if(!isAcqStarted()) {
				acqStart();
			}
		}
		else if(!strcasecmp(cmd, stopCmd)) {
			if(isAcqStarted()) {
				acqStop();
			}
		}
		else if(!strcasecmp(cmd, pauseCmd)) {
			if(!isAcqPaused()) {
				acqPause();
			}
		}
		else if(!strcasecmp(cmd, resumeCmd)) {
			if(isAcqPaused()) {
				acqResume();
			}
		}

		usleep(500);
	}

	return; 
}


void acqStart() {
	
	if(isAcqStarted()) {
		fprintf(stderr, "Warning: Data acquisition is already started.  %d\n", isAcqStarted());
		fflush(stderr);
		return; 
	}

	if(Standby_mode(master) != 1) {
		fprintf(stderr, "Error: Could not initialize data acquisition.\n");
		fflush(stderr);
		return;
	}

	if(Run_mode(master) != 0) {
		fprintf(stderr, "Error: Could not run data acquisition.\n");
		fflush(stderr);
		return;
	}
	
	showMessage(master, "%s", "Cmd: Start");
	startMonitorTask(master);
	return;
}

int isAcqStarted() {
	return !(master->globalShutdown);
}

void acqStop() {
	if(!isAcqStarted()) {
		fprintf(stderr, "Warning: Data acquistiion is already stopped.\n");
		fflush(stderr);
		return;
	}
	showMessage(master, "%s", "Cmd: Stop");
	master->globalShutdown = 1;
}

void acqPause() {
	if(!isAcqPaused()) {
		showMessage(master, "%s", "Cmd: Pause");
		master->globalPause = 1;
	}
}

void acqResume() {
	if(isAcqPaused()) {
		showMessage(master, "%s", "Cmd: Resume");
		master->globalPause = 0;
	}
}

int isAcqPaused() {
	return master->globalPause;
}

void acqQuit() {
	showMessage(master, "%s", "Cmd: Quit");
}

void printHelp() {
	fprintf(stderr, "USAGE: %s configFileName [-i] [-f fileName] [-d dirName] [-n scan] [-s start] [-e end] [-p step]\n", ACL_PROGNAM);
	fprintf(stderr, "\n\tVersion: %s\n", ACL_VERSION);
	fprintf(stderr, "\n\tArguments:\n");
	fprintf(stderr, "\t\tconfigFileName\t\tAn existing acqLib configuration file MUST be specified.\n");
	fprintf(stderr, "\n\tOptions:\n");
	fprintf(stderr, "\t\t-i\t\tInteractive Mode.  Read commands from standard input.\n");
	fprintf(stderr, "\t\t\t\tRecognized commands are 'start', 'stop', 'pause', 'resume' and 'quit'.\n");
	fprintf(stderr, "\t\t\t\tIf NOT specified, acquisition will immediately start.\n");
	fprintf(stderr, "\t\t-m macro\tMacro definition 'macro', with form 'key=value[,key=value][...]'.\n");
	fprintf(stderr, "\t\t-M macroFile\tRead macro definitions from file, 'macroFile'.\n");
	fprintf(stderr, "\t\t-f fileName\tSpecify the destination data file name. (ie: scanData%%03d.dat)\n");
	fprintf(stderr, "\t\t-d dirName\tSpecify the destination data directory.\n");
	fprintf(stderr, "\t\t-n scanName\tSet the current scan to the one with name, 'scanName'.\n");
	fprintf(stderr, "\t\t\t\tThe scan must already be specified in the configuration file.\n");
	fprintf(stderr, "\t\t-s start\tSet the start value to 'start' for current scan. (Must be preceded by -n option.)\n");
	fprintf(stderr, "\t\t-e end\t\tSet the end value to 'end' for current scan. (Must be preceded by -n option.)\n");
	fprintf(stderr, "\t\t-p step\t\tSet the step size to 'step' for current scan. (Must be preceded by -n option.)\n");
	fprintf(stderr, "\n");
	fflush(stderr);
	return;
}

void printVersion() {
	fprintf(stderr, "%s - Version %s\n", ACL_PROGNAM, ACL_VERSION);
}

void showMessage(acqMaster_t *master, char *fmt, ...) {
	va_list ap;
	char buf[256];
	va_start(ap, fmt);
	vsnprintf(buf, sizeof buf, fmt, ap);
	va_end(ap);
	printf("%s\n", buf);
	fflush(stdout);
}

void showStatus(acqMaster_t *master, acqState mode) {
	showMessage(master, "Mode: %s", curState(mode));
}

void showProgress(acqMaster_t * master, char *title, double percent) {
	showMessage(master, "completion: %g\n", percent);	
}

void onStop(acqMaster_t *master) {
	showMessage(master, "All done!\n");
}

void loadMacro(const char *macro) {
	parse_macros(macro, NULL);
}

void loadMacroFile(FILE *macroFile) {
	int last;
	char macroBuffer[MACRO_BUFFER_SIZE];
	while(fgets(macroBuffer, MACRO_BUFFER_SIZE, macroFile)) {
		if(macroBuffer[0] != '#') {
			last = strlen(macroBuffer) - 1;
			while(last > 0) {
				if((macroBuffer[last] == '\n') ||
				   (macroBuffer[last] == '\r')) {
					macroBuffer[last] = '\0';
					last--;
				} else {
					break;
				}
			}
			loadMacro(macroBuffer);
		}
	}
}

void printMacros() {
	macroTable *mt = macro_head();
	printf("Defined Macros:\n");
	while(mt) {
		printf("\t'%s'='%s'\n", mt->macroName, mt->macroVal);
		mt = mt->next;
	}
}
