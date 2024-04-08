<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>flow</title>

    <link rel="stylesheet" type="text/css" href="/gptflow/css/bootstrap/bootstrap-4.6.min.css" />
    <link rel="stylesheet" type="text/css" href="/gptflow/css/bootstrap/bootstrap.cust.css" />

    <script type="text/javascript" src="/gptflow/js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="/gptflow/js/page.js" ></script>
    <script type="text/javascript" src="/gptflow/js/bootstrap/bootstrap.min.js"></script>

    <script>

        function openWinCenter(openUrl, wWidth, wHeight) {
            if (!wWidth) {
                wWidth = 800;
            }
            if (!wHeight) {
                wHeight = 600;
            }
            var iTop = (window.screen.availHeight - 30 - wHeight) / 2; //获得窗口的垂直位置;
            var iLeft = (window.screen.availWidth - 10 - wWidth) / 2; //获得窗口的水平位置;
            return window.open(openUrl, "", "height=" + wHeight + ", width=" + wWidth + ", top=" + iTop + ", left=" + iLeft);
        }

        var __receivers = new Array();

        function selectData(url, ary, wWidth, wHeight) {
            if (ary && ary.length) {
                __receivers = ary;
                openWinCenter(url);
            }
        }

    </script>
</head>