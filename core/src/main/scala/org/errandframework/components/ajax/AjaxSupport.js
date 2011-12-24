/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

/**
 * ajaxRequest invokes the URL specified by the parameter and updates the DOM based on the results.
 * @param url the target URL.
 * @param elements the list of elements that should be sent with the request.  Each element can either be a form
 * or a control.  Passing a form sends all of the controls in that form with the exception of buttons.  Passing
 * a control sends just that control, which may be a button.
 */
function ajaxRequest(url, options) {
    console.log("Making AJAX request to: " + url);
    var request = new Request.HTML({
        url: url,
        onSuccess: function(responseTree, responseElements, responseHTML, responseJavaScript) {
            console.log("AJAX request returned " + responseTree.length + " elements");
            // For every HTML element in the response, replace the corresponding element in the DOM.
            Array.from(responseTree).each(function(element) {
                if (element.id)
                    element.replaces($(element.id));
            })
        },
        onFailure: function(xhr) {
            console.log("AJAX request failed: " + xhr.status + " " + xhr.statusText);
        }
    });
    request.post(options);
    return false;
}

function ajaxLink(url) {
    console.log("AJAX link: " + url);
    return ajaxRequest(url);
}

/**
 * Submit a form using AJAX.
 *
 * @param the button that the user clicked to submit the form.
 * MooTools doesn't submit buttons by default, so we add the button value to the query ourselves in order to
 * simulate what happens when the user submits a form using the regular browser mechanism.
 */
function ajaxButton(button) {
    console.log("AJAX button: " + button);
    var form = button.getParent('form');
    var queryString = form.toQueryString();
    if (button.name && button.value) {
        if (queryString)
            queryString += "&";
        queryString += button.name + '=' + encodeURIComponent(button.value);
    }
    return ajaxRequest(form.action, queryString);
}

function ajaxAction(url, elements) {
    console.log("AJAX action: " + url + ", " + elements);
    var queryString = [];
    elements.each(function(el) {
        var type = el.type;
        if (!el.name || el.disabled || type == 'submit' || type == 'reset' || type == 'file' || type == 'image') return;

        var value = (el.get('tag') == 'select') ? el.getSelected().map(function(opt) {
            // IE
            return document.id(opt).get('value');
        }) : ((type == 'radio' || type == 'checkbox') && !el.checked) ? null : el.get('value');

        Array.from(value).each(function(val) {
            if (typeof val != 'undefined') queryString.push(encodeURIComponent(el.name) + '=' + encodeURIComponent(val));
        });
    });
    return ajaxRequest(url, queryString.join('&'));
}



