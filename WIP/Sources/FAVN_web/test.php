
<!DOCTYPE html>
<html>
<head>
	<title></title>
</head>
<body>

	<form action="http://localhost/capston/WIP/Sources/FAVN_web/public/ambulanceupdate" method="POST">
		injury id : <input type="text" name="injury_id"><br/>
		asker : <input type="text" name="asker"><br/>
		asker_email : <input type="text" name="asker_email"><br/>
		title : <input type="text" name="title"><br/>
		content : <input type="text" name="content"><br/>
		<button type="submit">ask</button><br/>

		<!-- test login -->
		username : <input type="text" name="username"><br/>
		password : <input type="password" name="password"><br/>

		<button type="submit">login</button><br/>

		<!-- ambulance update -->
		id : <input type="text" name="id"><br/>
		latitude : <input type="text" name="latitude"><br/>
		longitude : <input type="text" name="longitude"><br/>
		status : <input type="text" name="status"><br/>
		caller : <input type="text" name="caller_taking_id"><br/>


		<button type="submit">update</button><br/>
	</form>

</body>
</html>


