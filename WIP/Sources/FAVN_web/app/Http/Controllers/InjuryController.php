<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\Injury;

use App\Instruction;

use App\LearningInstruction;

use Illuminate\Support\Collection;

class InjuryController extends Controller
{
    // Show view 'list of injury'
	public function showListInjuryView(){

		$injury = Injury::whereNull('isDeleted')
						->orWhere('isDeleted', '<>', 1)
						->paginate(5);

        return view('expert.injury.listinjury',  ['injury' => $injury]);
	}

	// Show view 'list of injury' by filter
 	public function showListInjuryViewByFilter($filterType){

		$injury = Injury::paginate(5);
        return view('expert.injury.listinjury',  ['injury' => $injury]);

        $injury = Injury::where('role_id', '=', $filterUser)->paginate(5);
			return view('admin.user.listuser', ['users' => $users, 'filterUser' => $filterUser]);
	}

	// Show view 'add injury'
	public function showAddInjuryView(){
		return view('expert.injury.addinjury');
	}

	// Add injury.
	public function addInjury(Request $request){
		// validate control data
		$this->validateInjuryInstruction($request);

		// Init object Injury 
		$injury = new Injury;

		// Assign injury value
		if($request->hasFile('injury_image')) {
			$image_original = $request->file('injury_image');
			$image_name = $image_original->getClientOriginalName();
			$image_name = str_random(4).'_'.$image_name;

			while (file_exists('upload/injury/images/'.$image_name)) {
				$image_name = str_random(4).'_'.$image_name;
			}

			$image_original->move('upload/injury/images', $image_name);
			$injury->image = $image_name;
		}
		$injury->injury_name = $request->injury_name;
		$injury->symptom = $request->symptom;
		$injury->priority = $request->priority;

		//Save injury to db
		$injury->save();

		$this->saveInstruction($request, $injury);

		$this->saveLearningInstruction($request, $injury);

		return redirect('expert/injury/addinjury')->with('noti', 'Đã thêm một chấn thương');
	}

	// Param : $count -> number of step of instructions
	private function validateInjuryInstruction($request){
		// validate control data
		$count_step_emergence = $request->count_step_emergence;
		$count_step_learning = $request->count_step_learning;
		$this->validate($request, 
			[
				'injury_name' => 'required|max:255|unique:injuries,injury_name',
				'symptom' => 'required|max:500',
			],
			[
				'injury_name.required' => 'Bạn phải nhập tên chấn thương.',
				'injury_name.max' => 'Tên chấn thương phải nhỏ hơn 255 kí tự.',
				'injury_name.unique' => 'Tên chấn thương đã tồn tại.',

				'symptom.required' => 'Bạn phải nhập triệu chứng.',
				'symptom.max' => 'Triệu chứng phải nhỏ hơn 500 kí tự.',
			]);

		// Validate emergency instruction 
		for ($i = 1; $i <= $count_step_emergence; $i++) { 
			$emergency_instruction_step = 'emergency_instruction_step'.$i;
			$this->validate($request, 
			[
				$emergency_instruction_step => 'required|max:500',
			],
			[

				$emergency_instruction_step.'.required' => 'Bạn phải nhập hướng dẫn sơ cứu bước '.$i,
				$emergency_instruction_step.'.max' => 'Hướng dẫn phải nhỏ hơn 500 kí tự.',
			]);
		}

		// Validate learning instruction
		for ($i = 1; $i <= $count_step_learning; $i++) { 
			$learning_instruction_step = 'learning_instruction_step'.$i;
			$learning_detail_step = 'learning_detail_step'.$i;
			$this->validate($request, 
				[
				$learning_instruction_step => 'required|max:500',
				$learning_detail_step => 'required|max:500'
				],
				[

				$learning_instruction_step.'.required' => 'Bạn phải nhập hướng dẫn học bước '.$i,
				$learning_instruction_step.'.max' => 'Hướng dẫn phải nhỏ hơn 500 kí tự.',

				$learning_detail_step.'.required' => 'Bạn phải nhập giải thích học bước '.$i,
				$learning_detail_step.'.max' => 'Giải thích phải nhỏ hơn 500 kí tự.'
				]);
		}
	}

	// Call when user add new injury and type is instruction
	// Param : $count -> number of step of instructions
	private function saveInstruction($request, $injury) {
		// Create and Asign instruction value
		$injury_id = $injury->id;
		$count = $request->count_step_emergence;

		for ($i=1; $i <= $count; $i++) { 
			// Temporary instruction and detail for each step
			$instruction_step = 'emergency_instruction_step'.$i;
			$isCall = "isCall".$i;

			$instruction = new Instruction;
			$instruction->injury_id = $injury_id;
			$instruction->step = $i;
			$instruction->content = $request->$instruction_step;
			if ($request->$isCall == 'on') {
				$instruction->isMakeCall = True;
			}
						
			// Temporary image control name for each step
			$image_step_controlName = 'emergency_image_step'.$i;
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
			$audio_step_controlName = 'emergency_audio_step'.$i;
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
	}

	// Call when user add new injury and type is learning instruction
	private function saveLearningInstruction($request, $injury) {
		// Create and Asign instruction value
		$injury_id = $injury->id;

		$count = $request->count_step_learning;
		
		for ($i=1; $i <= $count; $i++) { 
			// Temporary instruction and detail for each step
			$instruction_step = 'learning_instruction_step'.$i;
			$detail_step = 'learning_detail_step'.$i;

			$learningInstruction = new LearningInstruction;
			$learningInstruction->injury_id = $injury_id;
			$learningInstruction->step = $i;
			$learningInstruction->content = $request->$instruction_step;
			$learningInstruction->explain = $request->$detail_step;
			
			// Temporary image control name for each step
			$image_step_controlName = 'learning_image_step'.$i;
			if($request->hasFile($image_step_controlName)) {
				$image_step = $request->file($image_step_controlName);
				$image_name = $image_step->getClientOriginalName();
				$image_name = str_random(4).'_'.$image_name;

				while (file_exists('upload/injury/images/'.$image_name)) {
					$image_name = str_random(4).'_'.$image_name;
				}

				$image_step->move('upload/injury/images', $image_name);
				$learningInstruction->image = $image_name;

			}

			// Temporary audio control name for each step
			$audio_step_controlName = 'learning_audio_step'.$i;
			if($request->hasFile($audio_step_controlName)) {
				$audio_step = $request->file($audio_step_controlName);
				$audio_name = $audio_step->getClientOriginalName();
				$audio_name = str_random(4).'_'.$audio_name;

				while (file_exists('upload/injury/audios/'.$audio_name)) {
					$audio_name = str_random(4).'_'.$audio_name;
				}

				$audio_step->move('upload/injury/audios', $audio_name);
				$learningInstruction->audio = $audio_name;
			}

			// Save instruction of each step
			$learningInstruction->save();
		}
	}

	// Show view 'edit injury'
	public function showEditInjuryView($injury_id){
		$injury = Injury::find($injury_id);
		return view('expert.injury.editinjury',['injury'=>$injury]);
	}

	// Edit injury.
	public function editInjury(Request $request, $injury_id){
		// Find object Injury 
		$injury = Injury::find($injury_id);

		// validate control data
		$this->validateInjuryInstructionForEdit($request, $injury);

		// Assign injury value
		if($request->hasFile('injury_image')) {
			$image_original = $request->file('injury_image');
			$image_name = $image_original->getClientOriginalName();
			$image_name = str_random(4).'_'.$image_name;

			while (file_exists('upload/injury/images/'.$image_name)) {
				$image_name = str_random(4).'_'.$image_name;
			}

			$image_original->move('upload/injury/images', $image_name);
			$injury->image = $image_name;

		}
		$injury->injury_name = $request->injury_name;
		$injury->symptom = $request->symptom;
		$injury->priority = $request->priority;
		// Save injury to db
		$injury->save();

		$this->editEmergencyInstruction($injury, $request);

		$this->editLearningInstruction($injury, $request);

		return redirect('expert/injury/editinjury/'.$injury_id)->with('noti', 'Đã sửa chấn thương thành công');
	}

	private function editEmergencyInstruction($injury, $request) {
		$i = 0;
		foreach ($injury->instruction as $ins) {
			$i++;
			// Temporary instruction and detail for each step
			$instruction_step = 'emergency_instruction_step'.$i;
			$isCall = "isCall".$i;

			$ins->content = $request->$instruction_step;
			if ($request->$isCall == 'on') {
				$ins->isMakeCall = True;
			} else {
				$ins->isMakeCall = False;
			}
						
			// Temporary image control name for each step
			$image_step_controlName = 'emergency_image_step'.$i;
			if($request->hasFile($image_step_controlName)) {
				$image_step = $request->file($image_step_controlName);
				$image_name = $image_step->getClientOriginalName();
				$image_name = str_random(4).'_'.$image_name;

				while (file_exists('upload/injury/images/'.$image_name)) {
					$image_name = str_random(4).'_'.$image_name;
				}

				$image_step->move('upload/injury/images', $image_name);
				$ins->image = $image_name;

			}

			// Temporary audio control name for each step
			$audio_step_controlName = 'emergency_audio_step'.$i;
			if($request->hasFile($audio_step_controlName)) {
				$audio_step = $request->file($audio_step_controlName);
				$audio_name = $audio_step->getClientOriginalName();
				$audio_name = str_random(4).'_'.$audio_name;

				while (file_exists('upload/injury/audios/'.$audio_name)) {
					$audio_name = str_random(4).'_'.$audio_name;
				}

				$audio_step->move('upload/injury/audios', $audio_name);
				$ins->audio = $audio_name;
			}

			// Save instruction of each step
			$ins->save();
		}
	}

	private function editLearningInstruction($injury, $request) {
		$i = 0;
		foreach ($injury->learningInstruction as $ins) {
			$i++;
			$instruction_step = 'learning_instruction_step'.$i;
			$detail_step = 'learning_detail_step'.$i;

			$ins->content = $request->$instruction_step;
			$ins->explain = $request->$detail_step;

			// Temporary image control name for each step
			$image_step_controlName = 'learning_image_step'.$i;
			if($request->hasFile($image_step_controlName)) {
				$image_step = $request->file($image_step_controlName);
				$image_name = $image_step->getClientOriginalName();
				$image_name = str_random(4).'_'.$image_name;

				while (file_exists('upload/injury/images/'.$image_name)) {
					$image_name = str_random(4).'_'.$image_name;
				}

				$image_step->move('upload/injury/images', $image_name);
				$ins->image = $image_name;

			}

			// Temporary audio control name for each step
			$audio_step_controlName = 'learning_audio_step'.$i;
			if($request->hasFile($audio_step_controlName)) {
				$audio_step = $request->file($audio_step_controlName);
				$audio_name = $audio_step->getClientOriginalName();
				$audio_name = str_random(4).'_'.$audio_name;

				while (file_exists('upload/injury/audios/'.$audio_name)) {
					$audio_name = str_random(4).'_'.$audio_name;
				}

				$audio_step->move('upload/injury/audios', $audio_name);
				$ins->audio = $audio_name;
			}

			// Save instruction of each step
			$ins->save();
		}
	}

	// Param : $count -> number of step of instructions
	private function validateInjuryInstructionForEdit($request, $injury){
		// validate control data
		$count_step_emergence = $request->count_step_emergence;
		$count_step_learning = $request->count_step_learning;

		// Validate injury name when delete
		if($injury->injury_name != $request->injury_name){
			$this->validate($request, 
			[
				'injury_name' => 'unique:injuries,injury_name',
				
			],
			[
				'injury_name.unique' => 'Tên chấn thương đã tồn tại.',
			]);
		}

		$this->validate($request, 
			[
				'injury_name' => 'required|max:255',
				'symptom' => 'required|max:500',
			],
			[
				'injury_name.required' => 'Bạn phải nhập tên chấn thương.',
				'injury_name.max' => 'Tên chấn thương phải nhỏ hơn 255 kí tự.',
				'injury_name.unique' => 'Tên chấn thương đã tồn tại.',

				'symptom.required' => 'Bạn phải nhập triệu chứng.',
				'symptom.max' => 'Triệu chứng phải nhỏ hơn 500 kí tự.',
			]);

		// Validate emergency instruction 
		for ($i = 1; $i <= $count_step_emergence; $i++) { 
			$emergency_instruction_step = 'emergency_instruction_step'.$i;
			$this->validate($request, 
			[
				$emergency_instruction_step => 'required|max:500',
			],
			[

				$emergency_instruction_step.'.required' => 'Bạn phải nhập hướng dẫn sơ cứu bước '.$i,
				$emergency_instruction_step.'.max' => 'Hướng dẫn phải nhỏ hơn 500 kí tự.',
			]);
		}

		// Validate learning instruction
		for ($i = 1; $i <= $count_step_learning; $i++) { 
			$learning_instruction_step = 'learning_instruction_step'.$i;
			$learning_detail_step = 'learning_detail_step'.$i;
			$this->validate($request, 
				[
				$learning_instruction_step => 'required|max:500',
				$learning_detail_step => 'required|max:500'
				],
				[

				$learning_instruction_step.'.required' => 'Bạn phải nhập hướng dẫn học bước '.$i,
				$learning_instruction_step.'.max' => 'Hướng dẫn phải nhỏ hơn 500 kí tự.',

				$learning_detail_step.'.required' => 'Bạn phải nhập giải thích học bước '.$i,
				$learning_detail_step.'.max' => 'Giải thích phải nhỏ hơn 500 kí tự.'
				]);
		}
	}

	// Delete injury
	public function deleteInjury($injury_id){
		// Find injury
		$injury = Injury::find($injury_id);
		$injury_name = $injury->injury_name;

		// Delete instructions of this injury
		foreach ($injury->instruction as $ins) {
			$ins->isDeleted = True;
			$ins->save();
		}

		// Delete learning instruction of this injury
		foreach ($injury->learningInstruction as $ins) {
			$ins->isDeleted = True;
			$ins->save();
		}

		// Delete question of this injury
		foreach ($injury->question as $quest) {
			foreach ($quest->answer as $answer) {
				$answer->isDeleted = True;
				$answer->save();
			}
			$quest->isDeleted = True;
			$quest->save();
		}

		// Delete injury_name
		$injury->isDeleted = True;
		$injury->save();

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
