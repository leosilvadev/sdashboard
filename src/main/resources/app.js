import React from 'react';
import SockJS from 'sockjs-client';

const sock = new SockJS('http://localhost:8080/ws/dashboard');

sock.onopen = function() {
  console.log('open');
};

sock.onmessage = function(e) {
  console.log('message', e.data);
};

sock.onclose = function() {
  console.log('close');
};