/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     activityMeter.js
 *     
 */
function ActivityMeter(config) {
	this.config = config;
};

ActivityMeter.prototype.initialize = function() {
	if(this.img) {
		return true;
	}
	
	if(!this.config.url) {
		return false;
	}
	
	if(!this.config.applyTo) {
		return false;
	}
	
	var elm = document.getElementById(this.config.applyTo);
	if(!elm) {
		return false;
	}
		
	this.shift = 0;
	this.img = document.createElement("img");
	this.img.style.position = "relative";
	this.img.src = this.config.url;
	
	this.div = document.createElement("div");
	this.div.style.float = 'right';
	this.div.style.width = '128px';
	this.div.style.height = '12px';
	this.div.style.border = '1px solid black';
	this.div.style.overflow = 'hidden';
	this.div.appendChild(this.img);
	
	if(this.config.replace && elm.parentNode) {
		elm.parentNode.replaceChild(this.div, elm);
	} else {
		elm.appendChild(this.div);
	}
};

ActivityMeter.prototype.showActivity = function() {
	if(this.initialize()) {
		this.shift += 8;
		if (this.shift > 0) {
			this.shift = -128;
		}
		this.img.style.left = this.shift + "px";
	}
};
