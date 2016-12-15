<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

class DashBoardController extends Controller
{
    // Show view 'dashboard'
    public function showDashboardExpert(){
    	return view('expert.dashboard.dashboard');
    }

    public function showAdminDashboard() {
    	return view('admin.dashboard.dashboard');
    }

    public function showAdminNoti() {
    	return view('admin.noti.noti');
    }


}
