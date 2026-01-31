import PreviewPanel from "../components/PreviewPanel";
import EditPanel from "../components/EditPanel";
import FileExplorer from "../components/FileExplorer";
import CramMode from "../components/CramMode";
import { useState } from "react";

export default function Explorer() {
    const [mode, setMode] = useState("preview");
    const [selectedItem, setSelectedItem] = useState(null);
    const [selectedType, setSelectedType] = useState(null);

    function handleSelectFlashCard(item) {
        setMode("edit");
        setSelectedItem(item);
        setSelectedType("flashcard");
    }

    return (   
            <div>
                {mode === "preview" && <PreviewPanel />} 
                {mode === "edit" && <EditPanel item={selectedItem} type={selectedType} />} 
                {mode === "cram" && <CramMode />} 
                <FileExplorer onSelectItem={handleSelectFlashCard}/>
             </div>
    );
}