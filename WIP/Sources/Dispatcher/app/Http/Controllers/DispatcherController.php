<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\Injury;

use App\Ambulance;

class DispatcherController extends Controller
{
    //
	function showDispatcherView() {
		$injury = Injury::all();
		$ambulances = Ambulance::all();
		return view('dispatcher.layout.dispatcher', ['injury' => $injury, 'ambulances' => $ambulances]);
	}

}
