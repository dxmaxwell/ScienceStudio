/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     numberUtils.js
 *     
 */

function log10(x) {
	return Math.LOG10E * Math.log(x);
}

function round(number, decimal) {
	var adj = Math.pow(10, decimal);
	return Math.round(number * adj) / adj;
};


// Convert value to fixed or exponential format with specified precision.
// (For example if value = '1234.567' and precision = 2 then return '1200')
// Use with data reader (ie Ext.data.XmlReader)
//
function convertToPrecision(value, record, precision) {
	return Number(value).toPrecision(precision);
};

function convertToPrecision1(value, record) {
	return convertToPrecision(value, record, 1);
};

function convertToPrecision2(value, record) {
	return convertToPrecision(value, record, 2);
};

function convertToPrecision3(value, record) {
	return convertToPrecision(value, record, 3);
};

function convertToPrecision4(value, record) {
	return convertToPrecision(value, record, 4);
};

function convertToPrecision5(value, record) {
	return convertToPrecision(value, record, 5);
};

function convertToPrecision6(value, record) {
	return convertToPrecision(value, record, 6);
};

function convertToPrecision7(value, record) {
	return convertToPrecision(value, record, 7);
};

function convertToPrecision8(value, record) {
	return convertToPrecision(value, record, 8);
};

// Convert value to fixed decimal format with specified number of digits.
//(For example if value = '1234.567' and digits = 2 then return '1234.57')
// Use with data reader (ie Ext.data.XmlReader) 
//
function convertToFixed(value, record, digits) {
	return Number(value).toFixed(digits);
};

function convertToFixed0(value, record) {
	return convertToFixed(value, record, 0);
};

function convertToFixed1(value, record) {
	return convertToFixed(value, record, 1);
};

function convertToFixed2(value, record) {
	return convertToFixed(value, record, 2);
};

function convertToFixed3(value, record) {
	return convertToFixed(value, record, 3);
};

function convertToFixed4(value, record) {
	return convertToFixed(value, record, 4);
};

function convertToFixed5(value, record) {
	return convertToFixed(value, record, 5);
};

function convertToFixed6(value, record) {
	return convertToFixed(value, record, 6);
};

function convertToFixed7(value, record) {
	return convertToFixed(value, record, 7);
};

function convertToFixed8(value, record) {
	return convertToFixed(value, record, 8);
};

//Convert value to exponential decimal format with specified number of digits.
//(For example if value = '1234.567' and digits = 2 then return '1.2e+4')
//Use with data reader (ie Ext.data.XmlReader) 
//
function convertToExponential(value, record, digits) {
	return Number(value).toFixed(digits);
};

function convertToExponential0(value, record) {
	return convertToExponential(value, record, 0);
};

function convertToExponential1(value, record) {
	return convertToExponential(value, record, 1);
};

function convertToExponential2(value, record) {
	return convertToExponential(value, record, 2);
};

function convertToExponential3(value, record) {
	return convertToExponential(value, record, 3);
};

function convertToExponential4(value, record) {
	return convertToExponential(value, record, 4);
};

function convertToExponential5(value, record) {
	return convertToExponential(value, record, 5);
};

function convertToExponential6(value, record) {
	return convertToExponential(value, record, 6);
};

function convertToExponential7(value, record) {
	return convertToExponential(value, record, 7);
};

function convertToExponential8(value, record) {
	return convertToExponential(value, record, 8);
};

//
// Convert a time in seconds to HH:MM:SS or HH:MM
//
function convertSecondsToHHMMSS(value, record) {
	var seconds = Number(value);
	var sign = (seconds >= 0) ? 1 : -1;
	seconds = Math.abs(seconds);
	var hours = Math.floor(seconds / 3600);
	seconds -= (hours * 3600);
	var minutes = Math.floor(seconds / 60);
	seconds -= (minutes * 60);
	var hhmmss = (sign > 0) ? '' : '-';
	hhmmss += hours + ':';
	if(minutes < 10) { hhmmss += '0'; } 
	hhmmss += minutes + ':';
	if(seconds < 10) { hhmmss += '0'; }
	hhmmss += seconds;
	return hhmmss;
}

function convertSecondsToHHMM(value, record) {
	var seconds = Number(value);
	var sign = (seconds >= 0) ? 1 : -1;
	seconds = Math.abs(seconds);
	var hours = Math.floor(seconds / 3600);
	seconds -= (hours * 3600);
	var minutes = Math.floor(seconds / 60);
	seconds -= (minutes * 60);
	var hhmm = (sign > 0) ? '' : '-';
	hhmm += hours + ':';
	if(minutes < 10) { hhmm += '0'; } 
	hhmm += minutes;
	return hhmm;
}
