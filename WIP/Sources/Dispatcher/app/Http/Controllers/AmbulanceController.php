<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\Ambulance;

class AmbulanceController extends Controller
{
    //

	public function update(Request $request)
    {
        $dataJson = json_decode($request->getContent(), true);

        $ambulance = Ambulance::find($dataJson['id']);

        // Assign value from request
        $ambulance->team = $dataJson['team'];
        $ambulance->latitude = $dataJson['latitude'];
        $ambulance->longitude = $dataJson['longitude'];
        $ambulance->status = $dataJson['status'];
        $ambulance->caller_taking_id =$dataJson['caller_taking_id'];
        
        // Save to db
        $ambulance->save();

        echo "done";

    }

}
