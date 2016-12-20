<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\Ambulance;

use App\Caller;

class AmbulanceResourceController extends Controller
{
    //

    public function getAmbulances()
    {
        $ambulance = Ambulance::all();
        return Response(['ambulance' => $ambulance], 201);
    }

    public function isAvailableAmbulance()
    {
        $ambulances = Ambulance::where('status', '=', 'ready')
                         ->whereNotNull('latitude')
                         ->whereNotNull('longitude')
                         ->where(function ($query) {
                            $query->whereNull('isDeleted')
                                  ->orWhere('isDeleted', '<>', 1);
                          })
                        ->get();  
        if(count($ambulances) != 0) {
            echo true;
        } else {
            echo false;
        }

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

    public function declineTask($id) {
        $ambulance = Ambulance::find($id);
        $ambulance->status = "problem";
        $caller_id = $ambulance->caller_taking_id;
        $ambulance->caller_taking_id = null;
        $ambulance->save();

        if(isset($caller_id)) {
            $caller = Caller::find($caller_id);
            $caller->status = 'waiting';
            $caller->ambulance_user_id = null;
            $caller->save();
        }

        // UPDATE TO FIREBASE
        $this->updateFbAmbulance($id, 'problem', null);
    }

    public function readyToDoTask($id) {
        $ambulance = Ambulance::find($id);
        $ambulance->status = "ready";
        $ambulance->caller_taking_id = null;
        $ambulance->save();

        // UPDATE TO FIREBASE
        $this->updateFbAmbulance($id, 'ready', null);
    }

    public function acceptTask($id) {
        $ambulance = Ambulance::find($id);
        $ambulance->status = "buzy";
        $ambulance->save();

        $caller_id = $ambulance->caller_taking_id;
        if(isset($caller_id)) {
            $caller = Caller::find($caller_id);
            $caller->status = 'processing';
            $caller->ambulance_user_id = $ambulance->user_id;
            $caller->save();
        }

        // UPDATE TO FIREBASE
        $this->updateFbAmbulance($id, 'buzy', $caller_id);
    }

    public function successTask($id) {
        $ambulance = Ambulance::find($id);
        $ambulance->status = "ready";
        $caller_id = $ambulance->caller_taking_id;
        $ambulance->caller_taking_id = null;
        $ambulance->save();

        $caller = Caller::find($caller_id);
        if(isset($caller_id)) {
            $caller->status = 'success';
            $caller->ambulance_user_id = $id;
            $caller->save();
        }

        // UPDATE TO FIREBASE
        $this->updateFbAmbulance($id, 'ready', null);
    }

    function sendAmbulanceLocationService($id, $latitude, $longitude) {
        $ambulance = Ambulance::find($id);
        $ambulance->latitude = $latitude;
        $ambulance->longitude = $longitude;
        $ambulance->save();

        // UPDATE TO FIREBASE
        $this->undateFbAmbulanceLocation($id, $latitude, $longitude);

    }


    function undateFbAmbulanceLocation($ambulanceID, $latitude, $longitude) {
        $DEFAULT_URL = 'https://favn-e63df.firebaseio.com/';
        $DEFAULT_TOKEN = 'qlDIJ3a2P0Y5OBYiyV6krah7EUjaPeufwW6875CM';
        $firebase = new \Firebase\FirebaseLib($DEFAULT_URL, $DEFAULT_TOKEN);


        // --- storing an array ---
        $DEFAULT_PATH = 'ambulances/'. $ambulanceID .'/latitude/';
        $firebase->set($DEFAULT_PATH, $latitude);

        $DEFAULT_PATH = 'ambulances/'. $ambulanceID .'/longitude/';
        $firebase->set($DEFAULT_PATH, $longitude);
    }

    function updateFbAmbulance($ambulanceID, $status, $caller_taking_id) {
        $DEFAULT_URL = 'https://favn-e63df.firebaseio.com/';
        $DEFAULT_TOKEN = 'qlDIJ3a2P0Y5OBYiyV6krah7EUjaPeufwW6875CM';
        $firebase = new \Firebase\FirebaseLib($DEFAULT_URL, $DEFAULT_TOKEN);


        // --- storing an array ---
        $DEFAULT_PATH = 'ambulances/'. $ambulanceID .'/status/';
        $firebase->set($DEFAULT_PATH, $status);

        $DEFAULT_PATH = 'ambulances/'. $ambulanceID .'/caller_taking_id/';
        $firebase->set($DEFAULT_PATH, $caller_taking_id);

    }

    function cancelDispatch() {

    }

    function pickedCaller($ambulanceID) {
        
    }



  





}
