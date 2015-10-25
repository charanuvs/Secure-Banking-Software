<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>Payment</title>

    <style>

      html, body, #map-canvas {
        height: 100%;
        margin: 0px;
        padding: 0px
        background: rgba(237,237,237,1); background: -moz-linear-gradient(left, rgba(237,237,237,1) 0%, rgba(217,217,217,1) 32%, rgba(227,227,227,1) 52%, rgba(255,255,255,1) 100%); background: -webkit-gradient(left top, right top, color-stop(0%, rgba(237,237,237,1)), color-stop(32%, rgba(217,217,217,1)), color-stop(52%, rgba(227,227,227,1)), color-stop(100%, rgba(255,255,255,1))); background: -webkit-linear-gradient(left, rgba(237,237,237,1) 0%, rgba(217,217,217,1) 32%, rgba(227,227,227,1) 52%, rgba(255,255,255,1) 100%); background: -o-linear-gradient(left, rgba(237,237,237,1) 0%, rgba(217,217,217,1) 32%, rgba(227,227,227,1) 52%, rgba(255,255,255,1) 100%); background: -ms-linear-gradient(left, rgba(237,237,237,1) 0%, rgba(217,217,217,1) 32%, rgba(227,227,227,1) 52%, rgba(255,255,255,1) 100%); background: linear-gradient(to right, rgba(237,237,237,1) 0%, rgba(217,217,217,1) 32%, rgba(227,227,227,1) 52%, rgba(255,255,255,1) 100%); filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ededed', endColorstr='#ffffff', GradientType=1 );
        
      }

      button {
        padding: 8px 16px;
        font-size: 14px;
	    font-family: Georgia, serif;
      }
      	
      #paymentform {
      	position: absolute;
      	left: 5%;
      }
    
      #resetbutton {
      	position: relative;
      	margin-left: 17%;
      	margin-top: 29%;
      }
    </style>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.5/d3.min.js"></script>
    
    <title>PAYMENT</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>
    
    <link rel="stylesheet" href="https://d396qusza40orc.cloudfront.net/startup%2Fcode%2Fbootstrap-combined.no-icons.min.css">
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/3.0.2/css/font-awesome.css">
    <link rel="stylesheet" href="https://d396qusza40orc.cloudfront.net/startup%2Fcode%2Fsocial-buttons.css">
    <link href="http://fonts.googleapis.com/css?family=Ubuntu:300,400,500,700,300italic,400italic,500italic,700italic" rel="styleshe\
et" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,300,400,600,700,800"
    rel="stylesheet" type="text/css">

    </head>
  <body>
      	<h3>PROVIDE PAYMENT INFORMATION</h3>
      
      	<form id= 'paymentform' method = 'post' action='/user/payment/confirm'>

        	<label>From Username:</label>
        	<input type='text' id='fromusername'>

        	<label>From Account:</label>
        	<input type='number' id='fromaccvalue'>

        	<label>To Username:</label>
        	<input type='text' id='tousername'>
      		
        	<label>To Account:</label>
        	<input type='number' id='toaccvalue'>
      		
      		<label>Type</label>
      		<select id = 'type'>
      		<option>Payment</option>
      		<option>Transfer</option>      		
      		</select>
      		
        	<label>Amount:</label> 
        	<input type='number' id='amountvalue'>
      		<br>
      		<button id  = "requestpayment" style="margin-right:-10px;"type ="submit">REQUEST PAYMENT</button>
      		</form>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      		<button id  = "resetbutton" type ="submit" onclick = "return resetPaymentDialogValues();">Reset</button>
		
    <script>
  	function requestPayment(){
  		
  		console.log($("#fromaccvalue").val());
  		console.log($("#toaccvalue").val());
  		console.log($("#amountvalue").val());
  	}
  	
    function resetPaymentDialogValues(){
        $('#fromaccvalue').val("")
        $('#toaccvalue').val("")
        $('#amountvalue').val("")

    }
    
    </script>
  </body>
