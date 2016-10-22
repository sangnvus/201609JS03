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
   return view('expert.injury.addinjury');
});

Route::resource('question', 'question_controller');

Route::resource('injury', 'injury_controller');




// admin route group
Route::group(['prefix'=>'expert'], function(){
	// Dashboard
	Route::get('dashboard', 'DashBoardController@showDashboard');

	// Injury
	Route::group(['prefix'=>'injury'], function(){
		Route::get('listinjury', 'InjuryController@showListInjuryView');
		Route::get('editinjury', 'InjuryController@showEditInjuryView');
		Route::get('addinjury', 'InjuryController@showAddInjuryView');
		Route::post('addinjury', 'InjuryController@AddInjury');
	});

	// Q&A
	Route::group(['prefix'=>'qanda'], function(){
		Route::get('listqanda', 'qandacontroller@showListQandAView');
		Route::get('reply', 'qandacontroller@showRelyQandAView');
	});

	// Notification
	Route::group(['prefix'=>'noti'], function(){
		Route::get('listnoti', 'Noti@showListNotiView');
		Route::get('detailnoti', 'Noti@showDetailNotiView');
	});
});