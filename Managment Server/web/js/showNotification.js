/**
 * Created by Jacob on 6/2/14.
 */
$(function() {
    // Handler for .ready() called.
    new PNotify({
        title: 'Email was resent!',
        text: 'The Activation email was resent to your email',
        type: 'success'
    });
});