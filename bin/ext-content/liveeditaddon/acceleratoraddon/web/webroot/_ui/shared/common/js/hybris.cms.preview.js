$(document).ready(function ()
{
    parent.postMessage({eventName:'notifyIframeAboutUrlChange', data: [window.location.href, ACC.previewCurrentPagePk, ACC.previewCurrentUserId, ACC.previewCurrentJaloSessionId]},'*')
});