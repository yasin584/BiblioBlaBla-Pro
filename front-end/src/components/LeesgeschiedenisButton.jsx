import { useNavigate } from 'react-router-dom';


const LeesgeschiedenisButton = () => {
    const navigate = useNavigate();

    const handlLeesgeschiedenisButton = () => {
        navigate('/leesgeschiedenis');
    };

    return (
<button 
    onClick={handlLeesgeschiedenisButton}
    className=" w-full bg-gray-100 text-black px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-200 cursor-pointer"
>
    Leesgeschiedenis
</button>
    );
}

export default LeesgeschiedenisButton;