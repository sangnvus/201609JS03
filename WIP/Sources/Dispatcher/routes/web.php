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


Route::get('getreadyambulance', 'DispatcherController@returnReadyAmbulance');


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


// Ambulance decline tag
Route::get('declinetask/{id}', 'AmbulanceResourceController@declineTask');
// Ambulance decline tag
Route::get('readytodotask/{id}', 'AmbulanceResourceController@readyToDoTask');
// Ambulance decline tag
Route::get('accepttask/{id}', 'AmbulanceResourceController@acceptTask');
// Call Ambulance send location service
Route::get('sendlocation/{id}/{latitude}/{longitude}', 'AmbulanceResourceController@sendAmbulanceLocationService');
// Call ambulace picked caller service
Route::get('pickedcaller/{id}', 'AmbulanceResourceController@pickedCaller');
// Call logout service
Route::get('ambulancelogout/{id}', 'AmbulanceResourceController@logoutService');

// Call get caller by dispatcher service
Route::get('getcallerbydispatcher/{id}', 'CallerResourceController@returnCallerByDispatcher');





// Call is available ambulance
Route::get('isavailableambulance', 'AmbulanceResourceController@isAvailableAmbulance');


// Call cancel service
Route::get('canceldispatchservice/{id}', 'CallerResourceController@cancelDispatchService');



Route::get('ambulance', 'AmbulanceResourceController@getAmbulances');

Route::post('ambulanceUpdate', 'AmbulanceController@update');

// Update ambulance from app
Route::post('updatefromambulance', 'AmbulanceResourceController@updateFromAmbulance');


Route::post('callerUpdate', 'CallerResourceController@update');

Route::get('getcaller/{id}', 'CallerResourceController@returnCallerById');

// get caller by ambulance ID
Route::get('getcallerbyambulance/{id}', 'CallerResourceController@returnCallerTakingByAmbulanceID');


Route::get('getdistance', 'DispatcherController@dispatchACaller');


// Call controler show login view
Route::get('login', 'AuthController@showLoginView');

// Call controler handle login 
Route::post('login', 'AuthController@login');

// Call controler handle logout
Route::get('logout', 'AuthController@logout');

// Call controler handle logout
Route::get('dispatcher', 'DispatcherController@showDispatcherView')->middleware('dispatcherLogin');;

// accessdenied
Route::get('accessdenied', function(){
	return view('accessdenied');
});

// Autocomple search question
 Route::get('autocompletePhone', ['as' => 'autocompletePhone', 'uses' => 'CallerResourceController@autocomplete']);


// Dispatcher route
 Route::post('dispatch', 'DispatcherController@handleDispatch');





