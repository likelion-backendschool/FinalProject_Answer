toastr.options = {
    closeButton: false,
    debug: false,
    newestOnTop: false,
    progressBar: false,
    positionClass: "toast-top-right",
    preventDuplicates: false,
    onclick: null,
    showDuration: "300",
    hideDuration: "1000",
    timeOut: "5000",
    extendedTimeOut: "1000",
    showEasing: "swing",
    hideEasing: "linear",
    showMethod: "fadeIn",
    hideMethod: "fadeOut"
};

function parseMsg(msg) {
    const [pureMsg, ttl] = msg.split(";ttl=");

    const currentJsUnixTimestamp = new Date().getTime();

    if ( ttl && parseInt(ttl) + 5000 < currentJsUnixTimestamp ) {
        return [pureMsg, false];
    }

    return [pureMsg, true];
}

function successModal(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if ( needToShow ) {
        toastr["success"](pureMsg);
    }
}

function errorModal(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if ( needToShow ) {
        toastr["error"](pureMsg);
    }
}

function warningModal(msg) {
    const [pureMsg, needToShow] = parseMsg(msg);

    if ( needToShow ) {
        toastr["warning"](pureMsg);
    }
}