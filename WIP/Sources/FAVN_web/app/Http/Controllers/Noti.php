<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

class Noti extends Controller
{
    // Show view 'list noti'
    public function showListNotiView(){
    	return view('expert.notification.listnoti');
    }

    // Show view 'detail noti'
    public function showDetailNotiView(){
    	return view('expert.notification.detailnoti');
    }
}
