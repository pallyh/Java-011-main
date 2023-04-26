<%@ page import="itstep.learning.model.FormsModel" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    FormsModel model = (FormsModel) request.getAttribute("formsModel");
    String path = request.getContextPath();
%>
<html>
<head>
    <title>Forms</title>
    <style>
        main {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 4em;
        }

        form {
            padding: 1em;
        }

        form:nth-child(1) {
            background: darksalmon;
        }

        form:nth-child(2) {
            background: darkkhaki;
        }

        h2 {
            margin: 0;
        }

        div.form-control {
            display: flex;
            justify-content: space-between;
            margin-top: 10px;
        }

        div.form-control label,
        div.form-control input[type="date"],
        div.form-control input[type="color"],
        div.form-control input[type="submit"] {
            cursor: pointer;
        }

        input.string-input {
            margin-left: 1em;
            width: 12em;
        }

        input.number-input {
            width: 7em;
        }
    </style>
</head>
<body>
<h1>Передача данных</h1>
<main>
    <form action="<%= path %>/forms" method="get">
        <h2>Form GET</h2>
        <div class="form-control">
            <label for="string1">Enter string:</label>
            <input name="string" type="text" class="string-input" id="string1"/>
        </div>
        <div class="form-control">
            <label for="number1">Enter number:</label>
            <input name="number" type="number" step="0.01" class="number-input" id="number1"/>
        </div>
        <div class="form-control">
            <label for="date1">Enter date:</label>
            <input name="date" type="date" id="date1"/>
        </div>
        <div class="form-control">
            <label for="color1">Choose color:</label>
            <input name="color" type="color" id="color1"/>
        </div>
        <div class="form-control">
            <input type="submit" value="Submit GET"/>
        </div>
    </form>

    <form action="<%= path %>/forms" method="post">
        <h2>Form POST</h2>
        <div class="form-control">
            <label for="string2">Enter string:</label>
            <input name="string" type="text" class="string-input" id="string2"/>
        </div>
        <div class="form-control">
            <label for="number2">Enter number:</label>
            <input name="number" type="number" class="number-input" id="number2"/>
        </div>
        <div class="form-control">
            <label for="date2">Enter date:</label>
            <input name="date" type="date" id="date2"/>
        </div>
        <div class="form-control">
            <label for="color2">Choose color:</label>
            <input name="color" type="color" id="color2"/>
        </div>
        <div class="form-control">
            <input type="submit" value="Submit POST"/>
        </div>
    </form>
    <div>
        Method = <%= model.getMethod() %> <br>
        String = <%= model.getString() %> <br>
        Number = <%= model.getNumber() %> <br>
        Date = <%= model.getDate() %> <br>
        Color = <%= model.getColor() %> <br>
    </div>
</main>
</body>
</html>
