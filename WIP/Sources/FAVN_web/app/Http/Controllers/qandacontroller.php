<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

class qandacontroller extends Controller
{
    // Show view 'list Q & A'
    public function showListQandAView(){
    	return view('expert.qanda.listqanda');
    }

    // Show view 'reply Q & A'
    public function showRelyQandAView(){
    	return view('expert.qanda.reply');
    }
}
