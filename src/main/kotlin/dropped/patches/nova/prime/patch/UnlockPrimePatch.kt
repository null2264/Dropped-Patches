package app.revanced.patches.nova.prime.patch

import app.revanced.util.exception
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patches.nova.prime.fingerprints.UnlockPrimeFingerprint
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11x

@Patch(
    name = "Unlock prime",
    description = "Unlocks Nova Prime and all functions of the app.",
    compatiblePackages = [CompatiblePackage("com.teslacoilsw.launcher")]
)
object UnlockPrimePatch : BytecodePatch(
    setOf(UnlockPrimeFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        UnlockPrimeFingerprint.result?.apply {
        // Any value except 0 unlocks prime, but 512 is needed for a protection mechanism
        // which would reset the preferences if the value on disk had changed after a restart.
        val PRIME_STATUS: Int = 512
        val insertIndex = scanResult.patternScanResult!!.endIndex + 1

        val primeStatusRegister =
            (mutableMethod.implementation!!.instructions[insertIndex - 1] as BuilderInstruction11x).registerA

        mutableMethod.addInstruction(
            insertIndex,
            """
                const/16 v$primeStatusRegister, $PRIME_STATUS
            """
        )
    } ?: throw UnlockPrimeFingerprint.exception

  }
}
