/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

function AjaxTextField_handleKeyDown(event, menu, textField, url) {
    var keyCode = event.which;
    console.log("handleKeyDown: " + keyCode);
    // Anything selected now?
    var selected = menu.getFirst('li.selected');
    if (keyCode == 13) {
        // If something is selected, copy it to the entry field.
        if (selected) {
            textField.value = selected.innerText;
            AjaxTextField_hideMenu(menu);
            return false;
        }
        return true;
    } else if (keyCode == 40) {
        // If something is selected, then select the next sibling.
        // Otherwise, select the first one.
        if (selected) {
            if (selected.getNext()) {
                selected.removeClass('selected');
                selected.getNext().addClass('selected');
            }
        } else
            menu.getFirst('li').addClass('selected')
        return false;
    } else if (keyCode == 38) {
        // If something is selected, then select the previous sibling.
        // Otherwise, select the last one.
        if (selected) {
            if (selected.getPrevious()) {
                selected.removeClass('selected');
                selected.getPrevious().addClass('selected');
            }
        } else
            menu.getLast('li').addClass('selected')
        return false;
    } else {
        // Clear any existing timer and start a new one.
        clearTimeout(menu.retrieve('timer'));
        menu.store('timer', setTimeout(function() { AjaxTextField_requestMenuData(menu, textField.value, url); }, 250));
    }
    return true;
}

function AjaxTextField_requestMenuData(menu, value, url) {
    // Retrieve the value from last time we updated the menu, and don't send a request again
    // if the value hasn't changed.
    var lastValue = menu.retrieve('value', '');
    console.log("requestMenuData: " + value);
    if (!value)
        AjaxTextField_hideMenu(menu);
    else if (value != lastValue) {
        console.log("Loading AjaxTextField menu: " + value);
        var request = new Request.JSON({
            url: url,
            onSuccess: function(responseJSON, responseText) {
                var choices = responseJSON.choices;
                console.log("AjaxTextField menu request returned: " + choices);
                if (choices && choices.length > 0)
                    AjaxTextField_updateMenu(menu, choices, value);
                else
                    AjaxTextField_hideMenu(menu);
            },
            onFailure: function(xhr) {
                console.log("AjaxTextField menu request failed: " + xhr.status + " " + xhr.statusText);
            },
            secure: false
        });
        request.post('value=' + encodeURIComponent(value));
        return false;
    }
}

function AjaxTextField_updateMenu(menu, choices, value) {
    // If choices is empty or not provided, then hide the menu.
    console.log("updateMenu: " + choices);
    menu.empty();
    Array.from(choices).each(function(choice) {
        var item = new Element('li');
        item.innerText = choice;
        item.inject(menu);
    })
    menu.style.display = 'block';
    menu.store('value', value);
}

function AjaxTextField_hideMenu(menu) {
    console.log("hideMenu");
    // There may be a timer in progress, so make sure we ignore that.
    clearTimeout(menu.retrieve('timer'));
    // Both hide and empty the menu.  There's no reason to keep the selections.
    // If the user types again, the choices will be retrieved from the server.
    menu.style.display = 'none';
    menu.empty();
    menu.store('value', '');
}

