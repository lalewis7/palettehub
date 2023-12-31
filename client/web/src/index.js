// react libraries
import React from 'react';
import ReactDOM from 'react-dom/client';

// stylesheets
import './index.scss';

// entrypoint app component
import App from './App';

// CRA web vitals
import reportWebVitals from './utils/reportWebVitals';

// react browser
import { BrowserRouter } from 'react-router-dom';

// google auth
import { GoogleOAuthProvider } from '@react-oauth/google';

// context providers
import { TokenProvider } from './context/TokenProvider';
import { ColorModeProvider } from 'context/ColorModeProvider';

// redux
import store from './redux/store'
import { Provider } from 'react-redux'

// bootstrap
// import 'bootstrap/dist/css/bootstrap.css';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <GoogleOAuthProvider clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID}>
        <TokenProvider>
          <ColorModeProvider>
            <Provider store={store}>
              <App />
            </Provider>
          </ColorModeProvider>
        </TokenProvider>
      </GoogleOAuthProvider>
    </BrowserRouter>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals(console.log);
