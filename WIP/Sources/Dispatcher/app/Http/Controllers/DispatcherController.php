<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\Injury;

use App\Ambulance;

use App\Caller;

use Illuminate\Support\Facades\Redirect;

class DispatcherController extends Controller
{
    
	function showDispatcherView() {
		$injury = Injury::all();
		$ambulances = Ambulance::all();
		return view('dispatcher.layout.dispatcher', ['injury' => $injury, 'ambulances' => $ambulances]);
	}


	function handleDispatch(Request $request) {
		$caller = Caller::find($request->caller_id);

		$caller->dispatcher_user_id = $request->dispatcher_user_id;
		$caller->symptom = $request->symptom;
		$caller->save();

		if($caller == null) {
			return redirect('dispatcher')->with(['noti'=> 'noCaller'])->withInput(); 
		}

		$ambulances = Ambulance::where('status', '=', 'ready')
						 ->whereNotNull('latitude')
						 ->whereNotNull('longitude')
						 ->where(function ($query) {
			                $query->whereNull('isDeleted')
			                      ->orWhere('isDeleted', '<>', 1);
	                      })
						->get();
						
		$readyAmbulanceID = $this->getNearestAmbulanceID($ambulances, $caller);

		if($readyAmbulanceID == 0) {
			return redirect('dispatcher')->with(['noti'=> 'noAmbulance', 'caller' => $caller])->withInput(); 
		} else {
			$this->createAmbulanceTask($readyAmbulanceID, $request->caller_id);
			$ambulance = Ambulance::find($readyAmbulanceID);

			$caller->status = 'processing';
			$caller->ambulance_user_id = $ambulance->user_id;
			$caller->save();

			return redirect('dispatcher')->with(['ambulance'=> $ambulance, 'caller' => $caller])->withInput(); 
		}
	}


	function returnReadyAmbulance(Request $request) {
		$caller_id = $request->caller_id;
		$symptom = $request->symptom;
		$dispatcher_user_id = $request->dispatcher_user_id;

		$caller = Caller::find($caller_id);

		if($caller == null) {
			return 'nocaller';
		}

		// Save caller symptom and dispatcher that take
		$caller->dispatcher_user_id = $dispatcher_user_id;
		$caller->symptom = $symptom;
		$caller->save();

		$ambulances = Ambulance::where('status', '=', 'ready')
						 ->whereNotNull('latitude')
						 ->whereNotNull('longitude')
						 ->where(function ($query) {
			                $query->whereNull('isDeleted')
			                      ->orWhere('isDeleted', '<>', 1);
	                      })
						->get();
						
		$readyAmbulanceID = $this->getNearestAmbulanceID($ambulances, $caller);

		if($readyAmbulanceID == 0) {
			return 'noambulance'; 
		} else {
			$this->createAmbulanceTask($readyAmbulanceID, $request->caller_id);
			$ambulance = Ambulance::find($readyAmbulanceID);

			$caller->status = 'processing';
			$caller->ambulance_user_id = $ambulance->user_id;
			$caller->save();

			return ['ambulance' => $ambulance, 'caller' => $caller];
		}
	}


	function getListDistanceMatrix($ambulanceArray, $caller) {
		// $url = 'https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=21.022707,105.856471|21.019863,105.856042&destinations=21.024690,105.855967&key=AIzaSyBioI0HSh4p-EY1qGgSOKcsRm7vQjyIuuE';
		$baseUrl = 'https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial';
		$key = 'AIzaSyBioI0HSh4p-EY1qGgSOKcsRm7vQjyIuuE';
		$baseUrl .= '&origins=';

		// Add ambulanceArray
		for ($i=0; $i < $ambulanceArray->count(); $i++) { 
			$baseUrl .=  $ambulanceArray[$i]->latitude . ',' . $ambulanceArray[$i]->longitude;
			if ($i < $ambulanceArray->count() - 1) {
				$baseUrl .= '|';
			}
		}

		// Add caller
		$baseUrl .= '&destinations=' . $caller->latitude . ',' . $caller->longitude;

		// Add key for url 
		$baseUrl .= '&key=' . $key;

		$listDistanceResult = json_decode(file_get_contents($baseUrl))->rows;
		return $listDistanceResult;
	}

	function getNearestAmbulanceID($ambulances, $caller) {
		$listDistanceResult = $this->getListDistanceMatrix($ambulances, $caller);
		//var_dump($listDistanceResult);
		$listAmbulanceWithDistance = array();
		for ($i=0; $i < count($listDistanceResult); $i++) { 
			$key = $ambulances[$i]->id;
			$value = $listDistanceResult[$i]->elements[0]->distance->value;
			$listAmbulanceWithDistance["$key"] = $value;
		}
		asort($listAmbulanceWithDistance);
		$first_key = key($listAmbulanceWithDistance);
		return $first_key;
	}

	function createAmbulanceTask($ambulanceID, $caller_taking_id) {
		$ambulance = Ambulance::find($ambulanceID);

		// UPDATE TO MYSQL
		// Assign new status
		$ambulance->status = 'pending';
		$ambulance->caller_taking_id = $caller_taking_id;
		$ambulance->save();

		// UPDATE TO FIREBASE
		$this->updateFbAmbulance($ambulanceID, 'pending', $caller_taking_id);

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

	function updateMysqlAmbulanceStatus() {

	}

	function updateCallerStatus() {
		
	}




}
