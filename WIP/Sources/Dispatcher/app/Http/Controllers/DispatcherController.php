<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\Injury;

class DispatcherController extends Controller
{
    //
	function showDispatcherView() {
		$injury = Injury::all();
		return view('dispatcher.layout.dispatcher', ['injury' => $injury]);
	}

}
