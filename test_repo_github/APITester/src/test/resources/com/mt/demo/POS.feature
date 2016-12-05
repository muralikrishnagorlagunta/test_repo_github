Feature: Aadhar Card holder purchases and pays at POS
	As an Aadhar Card Holder
	I want to purchase goods at merchant shop and 
	authorize the payment using my Aadhar credentials
 
    @positive
	Scenario Outline: Happy path with Account has sufficient funds
		Given customer purchases for amount <amount> 
		When provides his Aadhar details "<aadharNo>" 
			And provides his finger print details "<fingerprint>"
		When the POS requests Aadhar Server for verification with "<posid>" 
		When the Aadhar Server gives back the token validate
		When the token is submitted to bank server 
		Then the transaction shall be successful 

 	
 	Examples:
 		|amount|aadharNo|fingerprint|posid|
 		|1000	|98237834          |ABCDgjxh123tq          |345|
		|2000	|98237834          |ABCDgjxh123tq          |345|
		|2500	|98237834          |ABCDgjxh123tq          |345|

    @negative
	Scenario Outline: Negative test for testing Man In The Middle attacks
		Given customer purchases for amount <amount> 
		When provides his Aadhar details "<aadharNo>" 
			And provides his finger print details "<fingerprint>"
		When the POS requests Aadhar Server for verification with "<posid>" 
			And if the token is invlidated
		When the Aadhar Server gives back the token validate
		When the token is submitted to bank server 
		Then the transaction should fail 

 	
 	Examples:
 		|amount|aadharNo|fingerprint|posid|
 		|1000	|98237834          |ABCDgjxh123tq          |345|
		|2000	|98237834          |ABCDgjxh123tq          |345|
		|2500	|98237834          |ABCDgjxh123tq          |345|

	@negative
	Scenario Outline: Negative tests for testing with invalid data
		Given customer purchases for amount <amount> 
		When provides his Aadhar details "<aadharNo>" 
			And provides his finger print details "<fingerprint>"
		When the POS requests Aadhar Server for verification with "<posid>" 
		When the token is submitted to bank server 
		Then the transaction should fail 

 	
 	Examples:
 		|amount|aadharNo|fingerprint|posid|
 		|1000	|98237448          |ABCDgjxh123tq          |345|
		|2000	|98237834          |ABCDgjxach12q          |345|
		|2500	|98237834          |ABCDgjxh123tq          |355|

    @negative
	Scenario Outline: Negative tests when transaction times out
		Given customer purchases for amount <amount> 
		When provides his Aadhar details "<aadharNo>" 
			And provides his finger print details "<fingerprint>"
		When the POS requests Aadhar Server for verification with "<posid>" 
			And wait for transaction to time-out
		When the Aadhar Server gives back the token validate
		When the token is submitted to bank server 
		Then the transaction should fail 

 	
 	Examples:
 		|amount|aadharNo|fingerprint|posid|
 		|1000	|98237834          |ABCDgjxh123tq          |345|
		|2000	|98237834          |ABCDgjxh123tq          |345|
		|2500	|98237834          |ABCDgjxh123tq          |345|
