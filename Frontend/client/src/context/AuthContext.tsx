import React, { createContext, useEffect, useReducer, ReactNode, Dispatch } from "react";

// Define the shape of the state
interface AuthState {
  user: any; // Replace `any` with your user type if you have one
  loading: boolean;
  error: any; // Replace `any` with your error type if you have one
}

// Define the shape of actions
type AuthAction =
  | { type: "LOGIN_START" }
  | { type: "LOGIN_SUCCESS"; payload: any } // Replace `any` with your user type if you have one
  | { type: "LOGIN_FAILURE"; payload: any } // Replace `any` with your error type if you have one
  | { type: "LOGOUT" };

// Initial state
const getStoredUser = (): any => { // Replace `any` with your user type if you have one
  try {
    const storedUser = localStorage.getItem("user");
    return storedUser ? JSON.parse(storedUser) : null;
  } catch (error) {
    return null;
  }
};

const INITIAL_STATE: AuthState = {
  user: getStoredUser(),
  loading: false,
  error: null,
};

// Create context with a default value
interface AuthContextProps extends AuthState {
  dispatch: Dispatch<AuthAction>;
}

export const AuthContext = createContext<AuthContextProps>({
  ...INITIAL_STATE,
  dispatch: () => null,
});

// Reducer function
const AuthReducer = (state: AuthState, action: AuthAction): AuthState => {
  switch (action.type) {
    case "LOGIN_START":
      return {
        user: null,
        loading: true,
        error: null,
      };
    case "LOGIN_SUCCESS":
      return {
        user: action.payload,
        loading: false,
        error: null,
      };
    case "LOGIN_FAILURE":
      return {
        user: null,
        loading: false,
        error: action.payload,
      };
    case "LOGOUT":
      return {
        user: null,
        loading: false,
        error: null,
      };
    default:
      return state;
  }
};

// Provider component
interface AuthProviderProps {
  children: ReactNode;
}

export const AuthContextProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [state, dispatch] = useReducer(AuthReducer, INITIAL_STATE);

  useEffect(() => {
    localStorage.setItem("user", JSON.stringify(state.user));
  }, [state.user]);

  return (
    <AuthContext.Provider value={{ ...state, dispatch }}>
      {children}
    </AuthContext.Provider>
  );
};
