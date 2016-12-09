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


Route::get('write', 'DispatcherController@writeToFirebase');



Route::get('/', function () {
   return view('login');
});

// For test
Route::get('getpass', function(){
	echo bcrypt('admin').'<br>';
	echo bcrypt('expert').'<br>';
	echo bcrypt('dispatcher').'<br>';
	echo bcrypt('ambulance').'<br>';
});


// ----- Resouce
Route::resource('ambulance', 'AmbulanceResourceController');
// ----- Resouce

Route::post('ambulanceUpdate', 'AmbulanceController@update');


Route::post('callerUpdate', 'CallerController@update');

Route::get('getCaller/{id}', 'CallerController@returnCallerById');

Route::get('getdistance', 'DispatcherController@dispatchACaller');


// Call controler show login view
Route::get('login', 'AuthController@showLoginView');

// Call controler handle login 
Route::post('login', 'AuthController@login');

// Call controler handle logout
Route::get('logout', 'AuthController@logout');

// Call controler handle logout
Route::get('dispatcher', 'DispatcherController@showDispatcherView');

// accessdenied
Route::get('accessdenied', function(){
	return view('accessdenied');
});

// Autocomple search question
 Route::get('autocompletePhone', ['as' => 'autocompletePhone', 'uses' => 'CallerController@autocomplete']);


// Dispatcher route
 Route::post('dispatch', 'DispatcherController@handleDispatch');



// Admin route group
Route::group(['prefix'=>'admin', 'middleware' => 'adminLogin'], function(){

	Route::group(['prefix'=>'user'], function(){

		// Call controller show list all user
		Route::get('listuser', 'UserController@showListUserView');

		Route::get('listuser/{filtertype}', 'UserController@showListUserViewByFilter');

		// Call controller show view 'edit user'
		Route::get('edituser/{id}', 'UserController@showEditUserView');

		// Call controller perform edit user
		Route::post('edituser/{id}', 'UserController@editUser');

		// Call controller how view 'adduser'
		Route::get('adduser', 'UserController@showAddUserView');

		// Call controller perform add user
		Route::post('adduser', 'UserController@addUser');

		// Call controller to delete user 
		Route::get('deleteuser/{id}', 'UserController@deleteUser');

		// Call controller search user
		Route::get('search', 'UserController@search');

	});	

	// Q&A
	Route::group(['prefix'=>'qanda'], function(){

		Route::get('listqanda/{filtertype}', 'qandacontroller@showListQandAView');

		Route::get('listqanda', 'qandacontroller@showListQandAView');

		// Route::get('reply/{question_id}', 'MailController@sendReplyMail');
		Route::get('reply/{question_id}', 'qandacontroller@showRelyQandAView');

		// Call controller to add answer of user question
		Route::post('reply/{question_id}', 'qandacontroller@addAnswer');

		// Call controller to delete q & a 
		Route::get('deletequestion/{question_id}', 'qandacontroller@deleteQuestion');

		// Call controller search innury
		Route::get('search', 'qandacontroller@search');


	});

});





// expert route group 
Route::group(['prefix'=>'expert', 'middleware' => 'expertLogin'], function(){
	// Dashboard
	Route::get('dashboard', 'DashBoardController@showDashboard');

	// Injury
	Route::group(['prefix'=>'injury'], function(){

		// Call controller show list all injury
		Route::get('listinjury', 'InjuryController@showListInjuryView');

		// Call controller show list all injury
		Route::get('listinjury/{filtertype}', 'InjuryController@showListInjuryViewByFilter');

		// Call controller show view 'edit injury'
		Route::get('editinjury/{injury_id}', 'InjuryController@showEditInjuryView');

		// Call controller perform edit injury
		Route::post('editinjury/{injury_id}', 'InjuryController@editInjury');

		// Call controller how view 'addinjury'
		Route::get('addinjury', 'InjuryController@showAddInjuryView');

		// Call controller perform add injury
		Route::post('addinjury', 'InjuryController@addInjury');

		// Call controller to delete injury 
		Route::get('deleteinjury/{injury_id}', 'InjuryController@deleteInjury');

		// Call controller search innury
		Route::get('search', 'InjuryController@search');

	});

	// Q&A
	Route::group(['prefix'=>'qanda'], function(){

		Route::get('listqanda/{filtertype}', 'qandacontroller@showListQandAView');

		Route::get('listqanda', 'qandacontroller@showListQandAViewbyAll');

		// Route::get('reply/{question_id}', 'MailController@sendReplyMail');
		Route::get('reply/{question_id}', 'qandacontroller@showRelyQandAView');

		// Call controller to add answer of user question
		Route::post('reply/{question_id}', 'qandacontroller@addAnswer');

		// Call controller to delete q & a 
		Route::get('deletequestion/{question_id}', 'qandacontroller@deleteQuestion');

		// Call controller search innury
		Route::get('search', 'qandacontroller@search');


	});

	// Notification
	Route::group(['prefix'=>'noti'], function(){
		Route::get('listnoti', 'Noti@showListNotiView');
		Route::get('detailnoti', 'Noti@showDetailNotiView');
	});
});


