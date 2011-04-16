/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      A panel contains a canvas to display CCD image. It uses canvastext.js to display text
 *      message on the canvas.
 *
 */

/**
 *
 * @include "include.js"
 */

Ext.ux.CCDImageCanvas = Ext.extend(Ext.BoxComponent, {

	imageURL : null,
	//image : null,
	text : null,
	currentY : 0,
	autoEl : {
		tag : 'canvas',
		width : '512',
		height : '512'
	},

	onRender : function() {
		Ext.ux.CCDImageCanvas.superclass.onRender.apply(this, arguments);
	},

	drawText : function(text) {
		var canvas = this.el.dom;
		if (canvas) {
			var context = canvas.getContext('2d');
			CanvasTextFunctions.enable(context);
			var font = "sans";
			var fontsize = 20;
			context.strokeStyle = "rgba(0,0,0,1)";
			context.fillStyle = "rgba(240,255,240,0.8)";
			context.fillRect(0, 0, canvas.width, canvas.height);
			this.currentY = (this.currentY + canvas.height / 8) % canvas.height;
			context.drawTextCenter(font, fontsize, canvas.width / 2,
					this.currentY, this.text);
		}

	},

	setImageURL : function(url) {
		var canvas = this.el.dom;
		if (canvas) {
			if (url == this.imageURL) {
				return; // it is still loading
			} else {
				this.imageURL = url; // a new image
				if (this.imageURL) {
					canvas.img = new Image();
					canvas.img.onload = function() {
						var context = canvas.getContext('2d');
						context.drawImage(canvas.img, 0, 0);
					};
					canvas.img.onError = function() {
						this.imageURL = null;
					};
					canvas.img.onAbort = function() {
						this.imageURL = null;
					};
					canvas.img.src = this.imageURL;
				}
			}
		}
	},

	setText : function(text) {
		if (text != this.text) {
			this.text = text;
			this.drawText(text);
		}
	}
});
