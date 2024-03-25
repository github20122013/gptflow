<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>gptflow</title>

    <link href="/gptflow/css/bootstrap/bootstrap-4.6.min.css" rel="stylesheet" type="text/css"/>
    <link href="/gptflow/css/main.css" rel="stylesheet" type="text/css"/>

    <script src="/gptflow/js/page.js" language="javascript" type="text/javascript"></script>
    <script src="/gptflow/js/bootstrap/bootstrap.min.js" language="javascript" type="text/javascript"></script>
    <script src="/gptflow/js/jquery-3.3.1.min.js" language="javascript" type="text/javascript"></script>

    <style>
        body {
            background: white;
            text-align: left;
        }

        .savebox {
            width: 180px;
            font-size: 12px;
        }
    </style>
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