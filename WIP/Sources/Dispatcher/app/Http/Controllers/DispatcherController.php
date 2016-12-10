<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\Injury;

use App\Ambulance;

use App\Caller;

class DispatcherController extends Controller
{
    
	function showDispatcherView() {
		$injury = Injury::all();
		$ambulances = Ambulance::all();
		return view('dispatcher.layout.dispatcher', ['injury' => $injury, 'ambulances' => $ambulances]);
	}


	function handleDispatch(Request $request) {
		// TODO :
		$caller = Caller::find($request->caller_id);

		if($caller == null) {
			return redirect('dispatcher')->with(['noti'=> 'noCaller']); 
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
			return redirect('dispatcher')->with(['noti'=> 'noAmbulance', 'caller' => $caller]); 
		} else {
			$this->createAmbulanceTask($readyAmbulanceID);
			$ambulance = Ambulance::find($readyAmbulanceID);
			return redirect('dispatcher')->with(['ambulance'=> $ambulance, 'caller' => $caller]); 
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

	function createAmbulanceTask($ambulanceID) {
		$ambulance = Ambulance::find($ambulanceID);

		// UPDATE TO MYSQL
		// Assign new status
		$ambulance->status = 'pending';
		$ambulance->save();

		// UPDATE TO FIREBASE
		$this->updateFbAmbulanceStatus($ambulanceID, 'pending');

	}
	
	function updateFbAmbulanceStatus($ambulanceID, $status) {
		$DEFAULT_URL = 'https://favn-e63df.firebaseio.com/';
		$DEFAULT_TOKEN = 'qlDIJ3a2P0Y5OBYiyV6krah7EUjaPeufwW6875CM';
		$DEFAULT_PATH = 'ambulances/'. $ambulanceID .'/status/';

		$firebase = new \Firebase\FirebaseLib($DEFAULT_URL, $DEFAULT_TOKEN);

		// --- storing an array ---
		$firebase->set($DEFAULT_PATH, $status);

	}




}
