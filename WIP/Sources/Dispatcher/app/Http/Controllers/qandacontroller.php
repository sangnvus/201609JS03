<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use App\Question;

use App\Answer;

use Mail;

use App\Injury;

use Illuminate\Support\Collection;

use Illuminate\Support\Facades\Auth;

class qandacontroller extends Controller
{

    // Show view 'list Q & A'
   // public function showListQandAView($filtertype){
    	// $question = Question::paginate(5);

     //    if ($role == 1) {
     //        # code...
     //    }

    	// if($filtertype == 'all'){
    	// 	return view('expert.qanda.listqanda', ['question' => $question, 'filtertype' => $filtertype]);
    	//}
    	// elseif($filtertype == 'not_answered') {
     //        $question = Question::join('Answer', function($join)
     //        {
     //            $join->on('Question.question_id', '=', 'Answer.question_id')
     //            ->where('contacts.user_id', '>', 5);
     //        })
     //    ->get();

    		// $listNotAnswered = new Collection;
    		// foreach ($question as $quest) {
    		// 	if(count($quest->answer) <= 0){
    		// 		$listNotAnswered->push($quest);
      //               $listNotAnswered->paginate(5);
    		// 	}
    		// }
    		// return view('expert.qanda.listqanda', ['question' => $listNotAnswered, 'filtertype' => $filtertype]);
    	// } else{
    	// 	$listAnswered = new Collection;
    	// 	foreach ($question as $quest) {
    	// 			if(count($quest->answer) > 0){
    	// 			$listAnswered->push($quest);
     //                $listAnswered->paginate(5);
    	// 		}
    	// 	}
    	// 	return view('expert.qanda.listqanda', ['question' => $listAnswered, 'filtertype' => $filtertype]);
    	// }
   // }
    public function showListQandAView(){

        $role_id = Auth::user()->role_id;

        $question = Question::paginate(5);
        if ($role_id == 1) {
            return view('admin.qanda.listqanda', ['question' => $question]);
        } else {
            return view('expert.qanda.listqanda', ['question' => $question]);
        }
        
            
    }

    // Show view 'reply Q & A'
    public function showRelyQandAView($question_id){

        $role_id = Auth::user()->role_id;

        $question = Question::find($question_id);
        if ($role_id == 1) {
            return view('admin.qanda.reply', ['question' => $question]);
        } else {
            return view('expert.qanda.reply', ['question' => $question]);
        }
    }

    // Perform add an answer
    public function addAnswer(Request $request, $question_id){
    	$this->validate($request, 
			[
				'answer' => 'required|max:1000'
			],
			[
				'answer.required' => 'Bạn phải nhập câu trả lời.',
				'answer.max' => 'Câu trả lời phải nhỏ hơn 1000 kí tự.'
			]);

    	// Init answer object
    	$answer = new Answer;

    	// Assign value to answer object
    	$answer->question_id = $question_id;
    	$answer->user_id = Auth::user()->id;
    	$answer->answer = $request->answer;

    	// Save to db
    	$answer->save();

        // Change question count answer
        $question = Question::find($question_id);
        $question->setCountAnswer($question_id);

    	// Send mail to user
    	$asker_email = $question->asker_email;
    	$asker = $question->asker;
    	$data = ['asker' => $asker, 'asker_email' => $asker_email, 'answer' => $request->answer];

    	$this->sendReplyMail($data);

        $role_id = Auth::user()->role_id;
        if ($role_id == 1) {
            return redirect('admin/qanda/reply/'.$question_id)->with('noti', 'Đã trả lời người dùng.');    
        } else {
            return redirect('expert/qanda/reply/'.$question_id)->with('noti', 'Đã trả lời người dùng.');    
        }

    	
    }

    // Send mail to user
    public function sendReplyMail($data){
    	Mail::send(['text' => 'expert.qanda.replyMailContent'], $data, function($message) use ($data){
    		$message->to($data['asker_email'], $data['asker'])->subject('Mail was sent from FAVN');
    		$message->from('favn.firstaid@gmail.com', 'FAVN');
    	});
    }

    // Delete question
    public function deleteQuestion($question_id){
    	// Find question
		$question = Question::find($question_id);
		

		// Delete instructions of this injury
		foreach ($question->answer as $ans) {
			$ans->delete();
		}

		// Delete injury
		$question->delete();

        $role_id = Auth::user()->role_id;
        if ($role_id == 1) {
           return redirect('admin/qanda/listqanda')->with('noti', 'Xóa thành công câu hỏi.');
        } else {
            return redirect('expert/qanda/listqanda')->with('noti', 'Xóa thành công câu hỏi.');  
        }

		
    }

    // Search question
	public function search(Request $request){
		$keyword = $request->term;
		$queryKeyword = '%'.$keyword.'%';

		$question = Question::leftjoin('injuries', 'injuries.id', '=', 'questions.injury_id')
        ->where('questions.id', 'LIKE', $queryKeyword)
        ->orWhere('asker', 'LIKE', $queryKeyword)
        ->orWhere('asker_email', 'LIKE', $queryKeyword)
        ->orWhere('injury_name', 'LIKE', $queryKeyword)
        ->orWhere('content', 'LIKE', $queryKeyword)->paginate(5);

         $role_id = Auth::user()->role_id;
        if ($role_id == 1) {
          return view('admin.qanda.listqanda', ['question' => $question]);
        } else {
           return view('expert.qanda.listqanda', ['question' => $question]);
        }
    	
	}

    // // Autocomplete search
    // public function autocomplete(Request $request){
    //     $keyword = $request->term;
    //     $queryKeyword = '%'.$keyword.'%';

    //     $question = Question::leftjoin('injuries', 'injuries.id', '=', 'questions.injury_id')
    //     ->where('questions.id', 'LIKE', $queryKeyword)
    //     ->orWhere('asker', 'LIKE', $queryKeyword)
    //     ->orWhere('asker_email', 'LIKE', $queryKeyword)
    //     ->orWhere('injury_name', 'LIKE', $queryKeyword)
    //     ->orWhere('content', 'LIKE', $queryKeyword)->get();

    //     $result = array();
    //     foreach ($question as $key => $value) {
    //         $result[] = ['id'];
    //     }
    // }
}
