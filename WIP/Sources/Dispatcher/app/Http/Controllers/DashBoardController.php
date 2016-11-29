<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

class DashBoardController extends Controller
{
    // Show view 'dashboard'
    public function showDashboard(){
    	return view('expert.dashboard.dashboard');
    }
}
