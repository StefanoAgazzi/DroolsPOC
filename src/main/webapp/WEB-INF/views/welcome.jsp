<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Generali Drools POC</title>
</head>
<body>

<form method="post" action="saverule">
    <h4>Rule "Check Bakance"</h4>
    <br/>
    WHEN
    <br/>
    <br/>
    <input type="text" id="when-entity" name="when-entity">
    <br/>
    <br/>
    <select name="when-condition">
        <option value="lt"><</option>
        <option value="eq">=</option>
        <option value="gt">></option>
    </select>
    <br/>
    <br/>
    <input type="text" id="when-condition-value" name="when-condition-value">
    <br/>
    <br/>
    THEN
    <br/>
    <br/>
    <input type="submit" value="SAVE RULE" id="save_rule"/>
</form>
<br/>
<br/>
<form method="post" action="runrule">
    <h4>Execute run on test data</h4>
    <input type="submit" value="RUN RULE" id="run_rule"/>
</form>
</body>
</form>
</html>