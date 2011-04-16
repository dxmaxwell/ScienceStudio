/**
 * Copyright (c) Canadian Light Source, Inc. All rights reserved. - see
 * license.txt for details.
 *
 * Description:
 * 		spectrumGraph.js
 *
 */


var spectrumGraph;

function spectrumGraphScaleUpdate(combo, record, index) {
	if(spectrumGraph) {
		var scale = record.get('value');
		spectrumGraph.updateOptions({
			logscale: (scale == 'LOG10')
		});
	}
};

function spectrumGraphDataUpdate(combo, record, index) {
	var x = spectrumPointXCmbBox.getValue();
	var y = spectrumPointYCmbBox.getValue();
	if(x && y) {
		Ext.Ajax.request({
			url:'../../scan/' + scanId + '/data/mca/spectrum.json',
			params:{ 'I':x, 'J':y },
			callback:spectrumGraphDataUpdateCallback
		});
	}
}; 

function spectrumGraphDataUpdateCallback(options, success, response) {
	
	if(!success) {
		Ext.Msg.alert('Error', 'An unknown error occurred while requesting spectrum data.');
		return;
	}
	
	var json = response.responseJson||Ext.decode(response.responseText, true);
	if(!json) {
		Ext.Msg.alert('Error', 'An unknown error occurred while parsing response.');
	}
		
	if(!json.success || !json.response.spectrum) {
		if(json.globalErrors && json.globalErrors[0]) {
			Ext.Msg.alert('Error', json.globalErrors[0]);
		} else {
			Ext.Msg.alert('Error', 'An unspecified error occurred while requesting spectrum data.');
		}
		return;
	}
	
	var data = [];
	var labelX = 'Energy';
	var labelY = 'Counts';
	
	var scale = spectrumScaleCmbBox.getValue();
	
	var minEnergy = 0.0;
	if(json.response.minEnergy) {
		minEnergy = json.response.minEnergy;
	}
	
	var maxEnergy = 1.0;
	if(json.response.maxEnergy) {
		maxEnergy = json.response.maxEnergy;
	}
	
	var spectrum = json.response.spectrum;
	var size = spectrum.length;
	if(size > 0) {
		var x0 = 1.0;
		var bin = 1.0;
		if(maxEnergy > minEnergy) {
			bin = (maxEnergy - minEnergy) / size;
			x0 = minEnergy + (bin / 2.0);
		}
		
		for (var i = 0; i < size; i++) {
			if(spectrum[i] < 1.0) {
				// Values less than one do not graph with logarithmic scale. // 
				data.push([x0 + (i * bin), 1.0 ]);
			} else {
				data.push([x0 + (i * bin), spectrum[i]]);
			}
		}
		
		// Dygraph will throw exception if the data set is empty.
		if(data.length == 0) {
			data.push([ 0, 0 ]);
		}
		
		if(spectrumGraph == null) {
			var elm = spectrumGraphBox.el.dom;
			if(elm) {
				spectrumGraph = new Dygraph(elm, data, {
					drawPoints: true,
					labels: [labelX, labelY],
					logscale: (scale == 'LOG10'),
					height:spectrumGraphPanel.getHeight(),
					width:spectrumGraphPanel.getWidth()
				});
			}
		} else {
			spectrumGraph.updateOptions({
				'file': data,
				'labels': [labelX, labelY]
			});
		}
	}
};
