<?php

namespace App\Http\Middleware;

use Closure;

use Illuminate\Support\Facades\Auth;

class DispatcherLoginMiddleware
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
         if (Auth::check()) { 
            $userLogin = Auth::user();
            if ($userLogin->role_id == 3) {
                view()->share('userLogin', $userLogin);
                return $next($request);
            }
        }
        return redirect('accessdenied');
    }
}
