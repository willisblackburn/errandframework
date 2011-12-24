/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

/**
 * Given a list item, transform it so it includes a hidden field and also a delete box.
 */
function BubbleField_transformItem(item, bubbleFieldName) {
    var hiddenField = new Element('input', {
        type: 'hidden',
        name: bubbleFieldName,
        value: item.innerText
    });
    hiddenField.inject(item);
    var closeLink = new Element('a', {
        text: 'X',
        href: '#',
        events: {
            click: function() {
                item.dispose();
            }
        }
    });
    closeLink.inject(item);
}

/**
 * Handles the keydown event inside the BubbleField text entry field.
 */
function BubbleField_handleKeyDown(event, bubbleList, textField, bubbleFieldName) {
    var keyCode = event.which;
    console.log("handleKeyDown: " + keyCode);
    // If it's return, and the user has typed some text, then add the text in the field as a new bubble.
    if (keyCode == 13 && textField.value) {
        var item = new Element('li');
        item.innerText = textField.value;
        BubbleField_transformItem(item, bubbleFieldName)
        item.inject(bubbleList);
        textField.value = '';
        return false;
    }
    return true;
}

