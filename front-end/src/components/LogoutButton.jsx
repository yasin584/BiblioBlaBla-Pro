import { useNavigate } from 'react-router-dom';

const LogoutButton = () => {
    const navigate = useNavigate();

    const handleLogout = () => {

        localStorage.removeItem('userToken'); 

        navigate('/login');
    };

    return (
        <button
            onClick={handleLogout}
            className=" w-full bg-red-600 text-black px-4 py-2 border border-gray-300 rounded-md cursor-pointer"
        >
            Uitloggen
        </button>
    );
};

export default LogoutButton;