<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\Ambulance;

class AmbulanceResourceController extends Controller
{
    //

    public function getAmbulances()
    {
        $ambulance = Ambulance::all();
        return Response(['ambulance' => $ambulance], 201);
    }

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

    public function updateFromAmbulance(Request $request)
    {

        $ambulance = Ambulance::find($request->input('id'));

        $ambulance->status = $request->input('status');
        $ambulance->latitude = $request->input('latitude');
        $ambulance->longitude = $request->input('longitude');
        $ambulance->caller_taking_id = $request->input('caller_taking_id');
        // Save to db
        $ambulance->save();

        echo 'Gửi câu hỏi thành công';

    }




}
