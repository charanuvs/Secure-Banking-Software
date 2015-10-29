$('#inter')
    .keyboard({
        layout: 'international',
        css: {
            // input & preview
            // "label-default" for a darker background
            // "light" for white text
            input: 'form-control input-sm dark',
            // keyboard container
            container: 'center-block well',
            // default state
            buttonDefault: 'btn btn-default',
            // hovered button
            buttonHover: 'btn-primary',
            // Action keys (e.g. Accept, Cancel, Tab, etc);
            // this replaces "actionClass" option
            buttonAction: 'active',
            // used when disabling the decimal button {dec}
            // when a decimal exists in the input area
            buttonDisabled: 'disabled'
        }
    })
    .addTyping();

// Script - typing extension
// simulate typing into the keyboard, use:
// \t or {t} = tab, \b or {b} = backspace, \r or \n or {e} = enter
// added {l} = caret left, {r} = caret right & {d} = delete
$('#inter-type').click(function () {
    $('#inter').getkeyboard().reveal().typeIn("{t}Hal{l}{l}{d}e{r}{r}l'o{b}o {e}{t}World", 500);
    return false;
});