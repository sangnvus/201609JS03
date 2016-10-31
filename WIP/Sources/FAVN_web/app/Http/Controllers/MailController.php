<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use Mail;

class MailController extends Controller
{
    // Reply user question
    public function sendReplyMail(){
    	$data = ['name' => 'Mai Trung Kien'];
    	Mail::send(['text' => 'expert.qanda.replyMailContent'], $data, function($message){
    		$message->to('kiencancode@gmail.com', 'Kien')->subject('Mail was sent from FAVN');
    		$message->from('favn.firstaid@gmail.com', 'FAVN');
    	});

    	echo 'sent';

    }

}
