/**
 * Helper to render notifications in UI
 */
var notifications = {

	/**
	 * Renders success message in UI
	 * @param message message to display
	 */
	success: function(message) {
		$("#alerts").html(this.renderAlert('success', 'Success', message));
	},

	/**
	 * Renders error message in UI
	 * @param message message to display
	 */
	error: function(message) {
		$("#alerts").html(this.renderAlert('danger', 'Error', message));
	},

	/**
	 * Returns HTML string for alert to insert into DOM
	 */
	renderAlert: function(alertClass, alertHeadline, alertMessage) {
		var alertHtml = '<div class="alert alert-' + alertClass + '" role="alert">' +
							'<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>' +
							'<strong>' + alertHeadline + '!</strong> ' + alertMessage +
						'</div>';
		return alertHtml;
	}
}