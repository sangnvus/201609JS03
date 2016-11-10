<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

use App\Answer;

class Question extends Model
{
    //
    protected $table = 'questions';

    public function injury(){
    	return $this->belongsTo('App\Injury');
    }

    public function answer(){
    	return $this->hasMany('App\Answer');
    }

    public function setCountAnswer($question_id){
    	$this->count_answer = Answer::where('question_id',$question_id)->count();
    	$this->save();
    }
}
