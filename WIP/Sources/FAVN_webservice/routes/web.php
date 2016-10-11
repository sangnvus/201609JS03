<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| This file is where you may define all of the routes that are handled
| by your application. Just tell Laravel the URIs it should respond
| to using a Closure or controller method. Build something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});

Route::get('talk', function() {
	echo 'Ha love Kien';
});


Route::get('addquestion', function() {
	$QUESTION = new App\QUESTION();
	$QUESTION->asker = 'Huong Nhat';
	$QUESTION->asker_email = 'Huong Nhat';
	$QUESTION->title = 'Huong Nhat';
	$QUESTION->content = 'Huong Nhat';

	$QUESTION->save();

	echo 'insert successfully';
});