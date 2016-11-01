<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\Injury;

use App\Instruction;

use Illuminate\Support\Collection;

class InjuryController extends Controller
{
    // Show view 'list of injury'
	public function showListInjuryView(Request $request){

		$injury = Injury::paginate(5);
        return view('expert.injury.listinjury',  ['injury' => $injury]);
	}

	// Show view 'add injury'
	public function showAddInjuryView(){
		return view('expert.injury.addinjury');
	}

	// Add injury.
	public function addInjury(Request $request){
		// validate control data
		$this->validate($request, 
			[
				'injury_name' => 'required|max:45|unique:injuries,injury_name',
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

		// Init object Injury 
		$injury = new Injury;

		// Assign injury value
		$injury->injury_name = $request->injury_name;
		$injury->symptom = $request->symptom;
		$injury->priority = $request->priority;
		//Save injury to db
		$injury->save();

		// Create and Asign instruction value
		$injury_id = $injury->id;
		$count = $request->count_step;
		for ($i=1; $i <= $count; $i++) { 
			// Temporary instruction and detail for each step
			$instruction_step = 'instruction_step'.$i;
			$detail_step = 'detail_step'.$i;

			// Check instruction step is null
			if (IsNullOrEmptyString($request->$instruction_step) or IsNullOrEmptyString($request->$detail_step)) {
			 	continue;
			}

			$instruction = new Instruction;
			$instruction->injury_id = $injury_id;
			$instruction->step = $i;
			$instruction->instruction = $request->$instruction_step;
			$instruction->explain_instruction = $request->$detail_step;
			
			// Temporary image control name for each step
			$image_step_controlName = 'image_step'.$i;
			if($request->hasFile($image_step_controlName)) {
				$image_step = $request->file($image_step_controlName);
				$image_name = $image_step->getClientOriginalName();
				$image_name = str_random(4).'_'.$image_name;

				while (file_exists('upload/injury/images/'.$image_name)) {
					$image_name = str_random(4).'_'.$image_name;
				}

				$image_step->move('upload/injury/images', $image_name);
				$instruction->image = $image_name;

			}

			// Temporary audio control name for each step
			$audio_step_controlName = 'audio_step'.$i;
			if($request->hasFile($audio_step_controlName)) {
				$audio_step = $request->file($audio_step_controlName);
				$audio_name = $audio_step->getClientOriginalName();
				$audio_name = str_random(4).'_'.$audio_name;

				while (file_exists('upload/injury/audios/'.$audio_name)) {
					$audio_name = str_random(4).'_'.$audio_name;
				}

				$audio_step->move('upload/injury/audios', $audio_name);
				$instruction->audio = $audio_name;
			}

			// Save instruction of each step
			$instruction->save();
		}

		return redirect('expert/injury/addinjury')->with('noti', 'Đã thêm một chấn thương');

	}

	// Show view 'edit injury'
	public function showEditInjuryView($injury_id){
		$injury = Injury::find($injury_id);
		return view('expert.injury.editinjury',['injury'=>$injury]);
	}

	// Edit injury.
	public function editInjury(Request $request, $injury_id){
		// validate control data
		$this->validate($request, 
			[
			'injury_name' => 'required|max:45',
			'symptom' => 'required|max:500',
			'instruction_step1' => 'required|max:500',
			'detail_step1' => 'required|max:500'
			],
			[
			'injury_name.required' => 'Bạn phải nhập tên chấn thương.',
			'injury_name.max' => 'Tên chấn thương phải nhỏ hơn 45 kí tự.',

			'symptom.required' => 'Bạn phải nhập triệu chứng.',
			'symptom.max' => 'Triệu chứng phải nhỏ hơn 500 kí tự.',

			'instruction_step1.required' => 'Bạn phải nhập hướng dẫn.',
			'instruction_step1.max' => 'Hướng dẫn phải nhỏ hơn 500 kí tự.',

			'detail_step1.required' => 'Bạn phải nhập giải thích.',
			'detail_step1.max' => 'Giải thích phải nhỏ hơn 500 kí tự.'
			]);

		// Find object Injury 
		$injury = Injury::find($injury_id);

		// Assign injury value
		$injury->injury_name = $request->injury_name;
		$injury->symptom = $request->symptom;
		$injury->priority = $request->priority;
		// Save injury to db
		$injury->save();

		// Delete old instructions
		foreach ($injury->instruction as $ins) {
			$ins->delete();
		}

		// Create and Asign instruction value
		$injury_id = $injury->id;
		$count = $request->count_step;
		for ($i=1; $i <= $count; $i++) { 
		// Temporary instruction and detail for each step
			$instruction_step = 'instruction_step'.$i;
			$detail_step = 'detail_step'.$i;

		// Check instruction step is null
			if (IsNullOrEmptyString($request->$instruction_step) or IsNullOrEmptyString($request->$detail_step)) {
				continue;
			}

			$instruction = new Instruction;
			$instruction->injury_id = $injury_id;
			$instruction->step = $i;
			$instruction->instruction = $request->$instruction_step;
			$instruction->explain_instruction = $request->$detail_step;

		// Temporary image control name for each step
			$image_step_controlName = 'image_step'.$i;
			if($request->hasFile($image_step_controlName)) {
				$image_step = $request->file($image_step_controlName);
				$image_name = $image_step->getClientOriginalName();
				$image_name = str_random(4).'_'.$image_name;

				while (file_exists('upload/injury/images/'.$image_name)) {
					$image_name = str_random(4).'_'.$image_name;
				}

				$image_step->move('upload/injury/images', $image_name);
				$instruction->image = $image_name;

			}

			// Temporary audio control name for each step
			$audio_step_controlName = 'audio_step'.$i;
			if($request->hasFile($audio_step_controlName)) {
				$audio_step = $request->file($audio_step_controlName);
				$audio_name = $audio_step->getClientOriginalName();
				$audio_name = str_random(4).'_'.$audio_name;

				while (file_exists('upload/injury/audios/'.$audio_name)) {
					$audio_name = str_random(4).'_'.$audio_name;
				}

				$audio_step->move('upload/injury/audios', $audio_name);
				$instruction->audio = $audio_name;
			}

			// Save instruction of each step
			$instruction->save();
		}

		return redirect('expert/injury/editinjury/'.$injury_id)->with('noti', 'Đã sửa chấn thương thành công');
	}

	// Delete injury
	public function deleteInjury($injury_id){
		// Find injury
		$injury = Injury::find($injury_id);
		$injury_name = $injury->injury_name;

		// Delete instructions of this injury
		foreach ($injury->instruction as $ins) {
			$ins->delete();
		}

		// Delete question of this injury
		foreach ($injury->question as $quest) {
			$quest->delete();
		}

		// Delete injury
		$injury->delete();

		return redirect('expert/injury/listinjury')->with('noti', 'Xóa thành công chấn thương '.$injury_name);

	}

	// Search injury
	public function search(Request $request){
		$keyword = $request->term;
		$queryKeyword = '%'.$keyword.'%';

		$injury = Injury::where('id', 'LIKE', $queryKeyword)
		->orWhere('injury_name', 'LIKE', $queryKeyword)
		->orWhere('symptom', 'LIKE', $queryKeyword)->paginate(5);
		
        return view('expert.injury.listinjury',  ['injury' => $injury]);
	}

}
