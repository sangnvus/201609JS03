<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\Caller;

use App\Ambulance;

class CallerResourceController extends Controller
{
    //
    public function autocomplete(Request $request) {
    	$term = $request->term;
    	$data = Caller::where('phone', 'LIKE', '%'.$term.'%')
                        ->where('status', 'LIKE', 'waiting')
                        ->get();
    	
    	$result = array();
    	foreach ($data as $key => $value) {
    		$result[] = ['id' => $value->id,
            'phone' => $value->phone,
            'injury_id' => $value->injury_id,
            'symptom' => $value->symptom,
            'latitude' => $value->latitude,
            'longitude' => $value->longitude,
            'status' => $value->status,
            'dispatcher_user_id' => $value->dispatcher_user_id,
            'ambulance_user_id' => $value->dispatcher_user_id,
            'label' => $value->phone];
    	}

    	return response()->json($result);
    }

    public function update(Request $request)
    {
        $dataJson = json_decode($request->getContent(), true);

        $caller = Caller::find($dataJson['id']);

        // Assign value from request
        $caller->injury_id = $dataJson['injury_id'];
        $caller->symptom = $dataJson['symptom'];
        $caller->latitude = $dataJson['latitude'];
        $caller->longitude = $dataJson['longitude'];
        $caller->status = $dataJson['status'];
        $caller->ambulance_user_id =$dataJson['ambulance_user_id'];
        
        // Save to db
        $caller->save();

        echo "done";

    }

    public function returnCallerById($id) {
        $caller = Caller::find($id);
        return Response($caller, 201);
    }

      // Service for dispatcher client
    function cancelDispatchService($caller_id) {
        $caller = Caller::find($caller_id);
        if(isset($caller_id)) {
            $caller->status = 'cancel';
            $caller->save();
        }
    }

    public function returnCallerTakingByAmbulanceID($id) {
        $Ambulance = Ambulance::find($id);
        if($Ambulance != null) {
            $caller = Caller::find($Ambulance->caller_taking_id);
            return Response($caller, 201);
        }
        
    }

    public function returnCallerByDispatcher($dispatcherID) {
        $caller = Caller::where('dispatcher_user_id', '=', $dispatcherID)
                        ->get();
        return $caller;
    }



}
