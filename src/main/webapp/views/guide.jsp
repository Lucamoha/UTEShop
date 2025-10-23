<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Hướng dẫn sử dụng</title>
    <script src="https://cdn.jsdelivr.net/npm/showdown@2.1.0/dist/showdown.min.js"></script>
</head>
<body class="container p-4">
    <div id="markdown-source" style="display:none;">${markdownContent}</div>
    <div id="markdown-html"></div>

    <script>
        const converter = new showdown.Converter({ tables: true, emoji: true });
        const markdown = document.getElementById("markdown-source").innerText;
        document.getElementById("markdown-html").innerHTML = converter.makeHtml(markdown);
    </script>
</body>
</html>
