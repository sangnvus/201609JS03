<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use Illuminate\Support\Facades\Auth;

class AmbulanceAuthController extends Controller
{
    // Handle login
	public function login(Request $request){

		$username = $request->username;
		$password = $request->password;

	 	if (Auth::attempt(['username' => $username, 'password' => $password])){
	 		if(Auth::user()->role_id == 3) {
	 			echo 'done';
	 		}
		}
	}


}
