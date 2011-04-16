#
#  Copyright (c) Canadian Light Source, Inc. All rights reserved.
#    - see license.txt for details.
# 
#   Description:
#     Makefile for command line interface for Data Acquisition library (libacq.so >= 3.2, libacqOutputHandler.so >= 3.2, libEpicsConnect.so >= 1.5)
#
#

EPICS_BASE_INC = $(EPICS_BASE)/include
EPICS_BASE_LIB = $(EPICS_BASE)/lib/$(HOST_ARCH)

ACQUISITON_INC = $(EPICS_LOCAL)/drivers/acquisition/3.2/include
ACQUISITON_LIB = $(EPICS_LOCAL)/drivers/acquisition/3.2/lib/$(HOST_ARCH)

ACQ_HANDLERS_INC = $(EPICS_LOCAL)/drivers/acq-handlers/3.2/include
ACQ_HANDLERS_LIB = $(EPICS_LOCAL)/drivers/acq-handlers/3.2/lib/$(HOST_ARCH)

EPICS_CONNECT_INC = $(EPICS_LOCAL)/drivers/epicsConnect/1.5/include
EPICS_CONNECT_LIB = $(EPICS_LOCAL)/drivers/epicsConnect/1.5/lib/$(HOST_ARCH)

CXXFLAGS += -Wall -I$(ACQUISITON_INC) -I$(ACQ_HANDLERS_INC) -I$(EPICS_CONNECT_INC) -I$(EPICS_BASE_INC)  -I$(EPICS_BASE_INC)/os/Linux
LDFLAGS += -L$(ACQUISITON_LIB) -L$(ACQ_HANDLERS_LIB) -L$(EPICS_CONNECT_LIB) -L$(EPICS_BASE_LIB)  -lEpicsConnect  -lacq  -lacqOutputHandler  -lCom   -lca  -ldl

.PHONY: help
help:
	@echo "USAGE: make [ acquisitionCL | test | clean | distclean ]"

acquisitionCL: acquisitionCL.cxx
	@echo Compiling acquisitionCL.cxx ...
	$(CXX) $(CXXFLAGS) -o $@ $< $(LDFLAGS)

.PHONY: test
test: acquisitionCL
	@echo Testing acquisitionCL...
	./acquisitionCL ./test/scan.cfg -M ./test/macros.txt -m "TEMPMONITOR=TM" -d ./test -f test%03d.dat -u 1 -n SRTempScan -s 1 -p 1 -e 10

.PHONY: clean
clean: cleanlib cleaninclude
	@echo Removing intermediate files...
	rm -f ./test/*.dat *~

.PHONY: cleanlib
cleanlib:
	@echo Removing 'lib' directory...
	rm -Rf ./lib

.PHONY: cleaninclude
cleaninclude:
	@echo Removing 'include' directory...
	rm -Rf ./include

.PHONY: distclean
distclean: clean
	@echo Removing executable file...
	rm -f acquisitionCL

#
#daq: daq.c
#	@echo Compiling daq.c ...
#	$(CC) $(CFLAGS) -o $@ $<  $(LDFLAGS)
#	@echo Done
#
#acquisitionCL: acquisitionCL.c
#	@echo Compiling acquisitionCL.c ...
#	$(CC)  $(CFLAGS) -o $@ $< $(LDFLAGS)
#	@echo Done
#
