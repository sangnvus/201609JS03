@extends('expert.layout.index')

@section('content')
	<div class="content">
	    <div class="container-fluid">
	        <div class="row">
	            <div class="col-md-4">
	                <div class="card">
	                    <div class="header">
	                        <h4 class="title">Q&A Statistics</h4>
	                        <p class="category">Statistics about questions, comment here</p>
	                    </div>
	                    <div class="content">
	                        <div id="chartPreferences" class="ct-chart ct-perfect-fourth"></div>

	                        <div class="footer">
	                            <div class="legend">
	                                <i class="fa fa-circle text-info"></i> Injury
	                                <i class="fa fa-circle text-danger"></i> Story
	                                <i class="fa fa-circle text-warning"></i> First aid
	                            </div>
	                            <hr>
	                            <div class="stats">
	                                <i class="fa fa-clock-o"></i> Month ago
	                            </div>
	                        </div>
	                    </div>
	                </div>
	            </div>

	            <div class="col-md-8">
	                <div class="card">
	                    <div class="header">
	                        <h4 class="title">Q&A Statistics</h4>
	                        <p class="category">Statistics about questions, comment here</p>
	                    </div>
	                    <div class="content">
	                        <div id="chartHours" class="ct-chart"></div>
	                        <div class="footer">
	                            <div class="legend">
	                                <i class="fa fa-circle text-info"></i> Injury
	                                <i class="fa fa-circle text-danger"></i> Story
	                                <i class="fa fa-circle text-warning"></i> First aid
	                            </div>
	                            <hr>
	                            <div class="stats">
	                                <i class="fa fa-history"></i> Month ago
	                            </div>
	                        </div>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
@endsection