@extends('expert.layout.index')

@section('content')
<div class="content">
    <div class="card">
        <div class="content">

            <!-- show errors and noti -->
            @if(count($errors) > 0)
            <div class="alert alert-danger">
                @foreach($errors->all() as $err)
                {{ $err }}<br/>
                @endforeach
            </div>
            @endif

            @if(session('noti'))
            <div class="alert alert-success">
                {{ session('noti') }}
            </div>
            @endif

            <form action="expert/qanda/reply/{{ $question->id }}" method="POST" class="form-horizontal">
                <input type="hidden" name="_token" value="{{ csrf_token() }}"/>
                <div class="form-group">
                    <label for="inputEmail3" class="col-sm-2 control-label">Người hỏi</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" value="{{ $question->asker }}" readonly>
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputEmail3" class="col-sm-2 control-label">Email</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" value="{{ $question->asker_email }}" readonly>
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputEmail3" class="col-sm-2 control-label">Tiêu đề</label>
                    <div class="col-sm-10">
                        @if(count($question->injury) > 0)
                            <input type="text" class="form-control" value="{{ $question->injury->injury_name }}" readonly>
                        @else
                            <input type="text" class="form-control" value="{{ $question->title }}" readonly>
                        @endif
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputEmail3" class="col-sm-2 control-label">Nội dung câu hỏi</label>
                    <div class="col-sm-10">
                        <textarea class="form-control" rows="3" readonly>{{ $question->content }}</textarea>
                    </div>
                </div>

                @foreach($question->answer as $ans)
                    <div class="form-group">
                        <label for="inputEmail3" class="col-sm-2 control-label">Trả lời vào : {{ $ans->created_at }}</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" rows="3" readonly>{{ $ans->answer }}</textarea>
                        </div>
                    </div>
                @endforeach

                <hr>

                <div class="form-group">
                    <label for="inputEmail3" class="col-sm-2 control-label">Trả lời</label>
                    <div class="col-sm-10">
                        <textarea class="form-control" rows="3" placeholder="Trả lời..." name="answer"></textarea>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-2"></div>
                    <div class="col-sm-2">
                        <input type="submit" class="form-control btn btn-fill" value="Trả lời">    
                    </div>
                </div>
         
            </form>
        </div>
    </div>
</div> 
@endsection