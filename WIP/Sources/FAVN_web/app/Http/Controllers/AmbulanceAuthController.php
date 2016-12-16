<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use Illuminate\Support\Facades\Auth;

use App\Ambulance;

class AmbulanceAuthController extends Controller
{
    // Handle login
	public function login(Request $request){

		$username = $request->username;
		$password = $request->password;

	 	if (Auth::attempt(['username' => $username, 'password' => $password])){
	 		if(Auth::user()->role_id == 4) {
	 			$ambulance = Auth::user()->ambulance;
	 			$ambulance->status = 'ready';
	 			$ambulance->save();
	 			return Response($ambulance);
	 		} else {
	 			return Response(['accessdenied']);
	 		} 
		} else {
			return Response(['wrong']);
		}
	}


}
