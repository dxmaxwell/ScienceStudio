/**
 * Copyright (c) Canadian Light Source, Inc. All rights reserved. - see
 * license.txt for details.
 *
 * Description: A panel contains a canvas to display CCD image. It uses
 * canvastext.js to display text message on the canvas.
 *
 */

Ext.ux.CCDImageCanvas = Ext.extend(Ext.BoxComponent, {

			imageURL : null,
			text : null,
			imageStatus : null,
			currentY : 0,
			autoEl : {
				tag : 'canvas',
				width : '512',
				height : '512'
			},

			onRender : function() {
				Ext.ux.CCDImageCanvas.superclass.onRender
						.apply(this, arguments);
			},

			drawText : function() {
				var canvas = this.el.dom;
				var ctx = canvas.getContext('2d');
				CanvasTextFunctions.enable(ctx);
				var font = "sans";
				var fontsize = 20;
				ctx.strokeStyle = "rgba(0,0,0,1)";
				this.currentY = (this.currentY + canvas.height / 8)
						% canvas.height;
				if (this.currentY < canvas.height / 8) 
					this.currentY = (this.currentY + canvas.height / 8)
					% canvas.height;
				ctx.drawTextCenter(font, fontsize, canvas.width / 2,
						this.currentY, this.text);
			},

			fill : function() {
				var canvas = this.el.dom;
				var ctx = canvas.getContext('2d');
				ctx.fillStyle = "rgba(240,255,240,0.8)";
				ctx.fillRect(0, 0, canvas.width, canvas.height);
			},

			setImageURL : function(url) {
				var canvas = this.el.dom;
				var ctx = canvas.getContext('2d');
				if (this.imageURL && this.imageURL == url) 
					return; // do not load the same image
				this.imageURL = url;
				if (this.imageURL) {
					canvas.img = new Image();
					canvas.img.onload = function() {
						ctx.drawImage(canvas.img, 0, 0);
						this.imageStatus = 'done';
					};
					canvas.img.onError = function() {
						this.imageURL = null;
						this.imageStatus = 'error';
						this.setText(this.imageStatus);
					};
					canvas.img.onAbort = function() {
						this.imageURL = null;
						this.imageStatus = 'abort';
						this.setText(this.imageStatus);
					};
					canvas.img.src = this.imageURL;
				}
			},

			setText : function(text) {
				if (text != this.text) {
					this.text = text;
					this.fill();
					this.drawText();
				}
			},

			mark : function(text) {
				var canvas = this.el.dom;
				var ctx = canvas.getContext('2d');
				CanvasTextFunctions.enable(ctx);
				var font = "sans";
				var fontsize = 20;
				ctx.strokeStyle = "rgba(0,0,0,1)";
				this.currentY = canvas.height / 8;
				ctx.drawTextCenter(font, fontsize, canvas.width / 2,
						this.currentY, text);
			}

		});
