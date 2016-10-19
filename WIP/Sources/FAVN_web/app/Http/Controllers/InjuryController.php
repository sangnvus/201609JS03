<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Injury;
use App\Instruction;

class InjuryController extends Controller
{
    // Show view 'list of injury'
	public function showListInjuryView(){
		$injury = Injury::all();
		return view('expert.injury.listinjury',  ['injury' => $injury]);
	}

	// Show view 'add injury'
	public function showAddInjuryView(){
		return view('expert.injury.addinjury');
	}

	// Show view 'edit injury'
	public function showEditInjuryView(){
		return view('expert.injury.editinjury');
	}

	// Add injury. Request by post method
	public function addInjury(Request $request){
		// validate control data
		$this->validate($request, 
			[
				'injury_name' => 'required|max:45|unique:Injury,injury_name',
				'symptom' => 'required|max:500',
				'instruction_step1' => 'required|max:500',
				'detail_step1' => 'required|max:500'
			],
			[
				'injury_name.required' => 'Bạn phải nhập tên chấn thương.',
				'injury_name.max' => 'Tên chấn thương phải nhỏ hơn 45 kí tự.',
				'injury_name.unique' => 'Tên chấn thương đã tồn tại.',

				'symptom.required' => 'Bạn phải nhập triệu chứng.',
				'symptom.max' => 'Triệu chứng phải nhỏ hơn 500 kí tự.',

				'instruction_step1.required' => 'Bạn phải nhập hướng dẫn.',
				'instruction_step1.max' => 'Hướng dẫn phải nhỏ hơn 500 kí tự.',

				'detail_step1.required' => 'Bạn phải nhập giải thích.',
				'detail_step1.max' => 'Giải thích phải nhỏ hơn 500 kí tự.'
			]);
		echo "injury_name : ".$request->injury_name.'<br/>';
		echo "priority : ".$request->priority.'<br/>';
		echo 'symptom : '.$request->symptom.'<br/>';




		$count = $request->count_step;

		for ($i=1; $i <= $count; $i++) { 
			$instruction_step = "instruction_step".$i;
			echo 'step '.$i.' : '.$request->$instruction_step.'<br/>';
		}
	}
}
