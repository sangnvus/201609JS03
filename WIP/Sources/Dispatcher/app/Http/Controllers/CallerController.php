<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Caller;

class CallerController extends Controller
{
    //
    public function autocomplete(Request $request) {
    	$term = $request->term;
    	$data = Caller::where('phone', 'LIKE', '%'.$term.'%')->get();
    	
    	$result = array();
    	foreach ($data as $key => $value) {
    		$result[] = ['phone' => $value->phone, 'injury_id' => $value->injury_id, 'symptom' => $value->symptom, 'latitude' => $value->latitude, 'longitude' => $value->longitude, 'status' => $value->status, 'label' => $value->phone, 'value' => $value->phone];
    	}

    	return response()->json($result);
    }
}
